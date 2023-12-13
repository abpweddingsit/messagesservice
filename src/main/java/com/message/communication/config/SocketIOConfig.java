package com.message.communication.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.message.communication.controller.UsersServiceRestcontroller;
import com.message.communication.dataobjects.Action;
import com.message.communication.dataobjects.AudioVedioResponseMessage;
import com.message.communication.dataobjects.CalltimeResponse;
import com.message.communication.dataobjects.CheckUserStatusCheckResponse;
import com.message.communication.dataobjects.Data;
import com.message.communication.dataobjects.EndCallResponse;
import com.message.communication.dataobjects.Message;
import com.message.communication.dataobjects.MissedCallResponse;
import com.message.communication.dataobjects.Notification;
import com.message.communication.dataobjects.PushData;
import com.message.communication.dataobjects.VedioMessage;
import com.message.communication.dataobjects.VedioMessageAcceptResponse;
import com.message.communication.dataobjects.VedioMessageIceCandidateResponse;
import com.message.communication.dataobjects.VedioMessageInitResponse;
import com.message.communication.dataobjects.VedioMessageNegotiationResponse;
import com.message.communication.dataobjects.VedioMessageStartResponse;
import com.message.communication.dataobjects.VedioResponseMessage;
import com.message.communication.entity.ChatAudioVedioDetails;
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMaster;
import com.message.communication.entity.UserDeviceInfo;
import com.message.communication.external.FCMPushGatewayBusinessObject;
import com.message.communication.service.ChatUsersService;

import lombok.extern.log4j.Log4j2;

@CrossOrigin
@Component
@Log4j2
//@EnableAsync
public class SocketIOConfig {
	@Value("${socket.host}")
	private String SOCKETHOST;
	@Value("${socket.port}")
	private int SOCKETPORT;
	
	private SocketIOServer server;
	
	@Autowired
	ChatUsersService chatUsersService;
	
	@Autowired
	UsersServiceRestcontroller usersServiceRestcontroller;
	
	//ConcurrentMap<Long, Map<Long,UUID>> userIdToSocketIdMap = new ConcurrentHashMap<>();
	ConcurrentMap<Long, UUID> userIdToSocketIdMap = new ConcurrentHashMap<>();
	ConcurrentMap<Long, List<Long>> userIdToClientMap = new ConcurrentHashMap<>();
	ConcurrentMap<Long, Map<Long,UUID>> callRoomMap = new ConcurrentHashMap<>();
	ConcurrentMap<Long, UUID> socArrMap = new ConcurrentHashMap<>();
	//ConcurrentMap<String,String>  soctempArr = new ConcurrentHashMap<>();
	
	
	@Bean
	public SocketIOServer socketIOServer() {
		Configuration config = new Configuration();
		config.setHostname(SOCKETHOST);
		config.setPort(SOCKETPORT);
		server = new SocketIOServer(config);
		server.start();
		
		server.addConnectListener(new ConnectListener() {
			@Override
			public void onConnect(SocketIOClient client) {
				
				//log.info("new user 111 connected with socket " + client.getSessionId());
			}
		});

		server.addDisconnectListener(new DisconnectListener() {
			@Override
			public void onDisconnect(SocketIOClient client) {
				client.getNamespace().getAllClients().stream().forEach(data-> {
					//log.info("user disconnected "+data.getSessionId().toString());
					});
			}
		});
		
		server.addEventListener("registerUser", VedioMessage.class, (client, data, ackSender) -> {
			
			log.info("registerUser from client: " + data.toString());
			
			//List<UserFriendListDataObjects> list = chatUsersService.getUserFriendList(data.getFrom());
			
			
			server.addConnectListener(new ConnectListener() {
				@Override
				public void onConnect(SocketIOClient client) {
					
					//log.info("new user "+data.getFrom()+ " connected with socket in register user" );
				}
			});
			
			//Map<Long,UUID> userIdArr = new HashMap();
			//List<UUID> userIdArr = new ArrayList();
			
			
			if(userIdToSocketIdMap.containsKey(data.getFrom())) {
				//Map<Long,UUID> userIdArr1 = (Map<Long,UUID>) userIdToSocketIdMap.get(data.getFrom());
				
				//userIdToSocketIdMap.remove(data.getFrom());
				
				if(userIdToSocketIdMap!=null && userIdToSocketIdMap.size()>0) {
					//List<UUID> userIdArr1 = (List<UUID>) userIdToSocketIdMap.get(data.getFrom());
					//userIdArr.add(clientId);
					//userIdArr1.put(data.getFrom(), SocketIOClient.getSessionId());
					//if(userIdArr1!=null && userIdArr1.size()>0) {
						UUID clientId = client.getSessionId();
						//userIdArr1.add(clientId);
						//log.info("registerUser userIdArr size is in IF: " + userIdArr1.size());
						log.info("registerUser in clientId at IF : " + clientId + " userId "+data.getFrom());
						userIdToSocketIdMap.put(data.getFrom(), clientId);
						
						SocketIOClient targetClient = server.getClient(clientId);
						if(targetClient!=null) {
							log.info("registerUser clientId connected in IF: " + targetClient.isChannelOpen());
							targetClient.sendEvent("registerUserComplete");
						}
					//}
				}
			}
			else {
				UUID clientId = client.getSessionId();
				
				//userIdArr.put(data.getFrom(), clientId);
				//userIdArr.add(clientId);
				//userIdArr.add(clientId);
				//log.info("registerUser userIdArr size is in else: " + userIdArr.size());
				log.info("registerUser in clientId at else : " + clientId + " userId "+data.getFrom());
				userIdToSocketIdMap.put(data.getFrom(), clientId);
				
				SocketIOClient targetClient = server.getClient(clientId);
				if(targetClient!=null) {
					log.info("registerUser clientId connected in else: " + targetClient.isChannelOpen());
					targetClient.sendEvent("registerUserComplete");
				}
			}
			
			
			//ackSender.sendAckData("registerUser");
			//server.getBroadcastOperations().sendEvent("registerUser",client, data);
			
			
			
			
		});
		
		
		server.addEventListener("messageSendToUser", Message.class, (client, message, ackSender) -> {
			log.info("messageSendToUser from client: " + message.toString());
			
			List<ChatUsersMaster> userList = chatUsersService.getUsersByUserCode(message.getSenderName());
            List<ChatUsersMaster> mappedUserList = chatUsersService.getUsersByUserCode(message.getTargetUserName());
            
            Long from = userList.get(0).getUserid();
            Long to = mappedUserList.get(0).getUserid();
            
            log.info("messageSendToUser from "+from);
            log.info("messageSendToUser to "+to);
            
            
            UUID entry = (UUID) userIdToSocketIdMap.get(to);
            log.info("messageSendToUser entry "+entry);
            
            Map<String, Object> map = new HashMap<String, Object>();
            
            if(entry!=null) {
            	SocketIOClient targetClient = server.getClient(entry);
            	
            	if(targetClient!=null) {
            		log.info("messageSendToUser within entry "+entry+" to user  "+to+" in clientId connected: " + targetClient.isChannelOpen());
            		
            		targetClient.sendEvent(message.getTargetUserName(),message);
            		
            	}else {
                	List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(to);
                	
                	log.info("Check deviceinfoDetails size in messageSendToUser-->"+deviceinfoDetails.size());
                	
                	String pushMessage = "Someone has sent you a message";
                	
                	if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
                		//for(UserDeviceInfo userDeviceInfo : deviceinfoDetails) {
                		UserDeviceInfo userDeviceInfo = (UserDeviceInfo)deviceinfoDetails.get(0);
                		  if(userDeviceInfo.getPlatform().equals("android") || userDeviceInfo.getPlatform().equals("ios") || userDeviceInfo.getPlatform().equals("iPhone")) {
                			//PUSH Notification
    		            	FCMPushGatewayBusinessObject fCMPushGatewayBusinessObject = new FCMPushGatewayBusinessObject();
    		            	Notification notification = new Notification();
    		            	
    		            	Data data = new Data();
    		            	data.setSource("AET");
    		            	data.setBody(pushMessage);
    		            	
    		            	String device_id = userDeviceInfo.getPushtoken();  //device id of target
    		            	
    		            	fCMPushGatewayBusinessObject.sendAndroidCampaignPushMessage(device_id,notification,data,from);
                		 }/*else {
                			//WEB Push Notification
                			webPushApplicationService.sendNotifications(pushMessage);
                		 }*/
                	  //}
                	}
                	
                }
              }
            	map.put("userid", userList.get(0).getUserid());
                map.put("mappeduserid", mappedUserList.get(0).getUserid());
                map.put("message", message.getMessage());
                map.put("type", message.getType());
                map.put("devPlatform", message.getDevPlatform());
                
                usersServiceRestcontroller.usersFriendListUpdation(map);
                
                chatUsersService.callJabberSave(userList.get(0).getUserid(), mappedUserList.get(0).getUserid(), message.getMessage());
            
		});
		
		
		server.addEventListener("initCall", VedioMessage.class, (client, data, ackSender) -> {
			
            // Handle startcall event
			//log.info("initCall call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("initCall Data: " + data.toString());
						
			try {
			ChatAudioVedioDetails chatAudioVedioDetails = new ChatAudioVedioDetails();
			
			chatAudioVedioDetails.setUserid(data.getFrom());
			chatAudioVedioDetails.setMappeduserid(data.getTo());
			chatAudioVedioDetails.setInittime(System.currentTimeMillis());
			chatAudioVedioDetails.setCalltype(data.getCalltype());
			chatAudioVedioDetails.setCallduration("missedcall");
			
			chatUsersService.saveAudiovedioMasters(chatAudioVedioDetails);
			
			/*ChatMessageDetails chatMessageDetails = new ChatMessageDetails();
			chatMessageDetails.setUserid(data.getFrom());
			chatMessageDetails.setMappeduserid(data.getTo());
			chatMessageDetails.setIsreadmsg(1);
			chatMessageDetails.setCreatedon(System.currentTimeMillis());
			chatMessageDetails.setCalltype(data.getCalltype());
			chatMessageDetails.setCallduration("missedcall");
			chatMessageDetails.setDevplatform(data.getDevplatform());
			
			chatUsersService.saveChatMessegeDetails(chatMessageDetails);*/
			
			/*boolean alreadyengageFlag = false;
			
			if(callRoomMap!=null && callRoomMap.size()>0) {
				for (Map.Entry<Long, Map<Long,UUID>> callRoomMapentry : callRoomMap.entrySet()) {
					Map<Long, UUID> socArrMap = (Map<Long, UUID>) callRoomMap.get(callRoomMapentry.getKey());
					
					
				  for (Map.Entry<Long, UUID> socArrMapentry : socArrMap.entrySet()) {
					 log.info("initCall within engagement UserId : " + socArrMapentry.getKey() + " UUID "+socArrMapentry.getValue());
					 
					 if(socArrMapentry.getKey().equals(data.getTo()) || socArrMapentry.getKey() == data.getTo()) {
						 
					   log.info("initCall within engagement Actual toUserId : " + socArrMapentry.getKey() + " UUID "+socArrMapentry.getValue());
					   UUID engageUUID = socArrMapentry.getValue();
					   SocketIOClient engagetargetClient = server.getClient(engageUUID);
					 
					  if(engagetargetClient!=null) {
						  
						log.info("initCall engagetargetClient connected: " + engagetargetClient.isChannelOpen());
						alreadyengageFlag = true;
					 }
				  }
			    }
			  }
			}
			log.info("initCall alreadyengageFlag is: " + alreadyengageFlag);*/
		
		    /*if(alreadyengageFlag == true) {
			    		UUID entry = (UUID) userIdToSocketIdMap.get(data.getFrom());
			    		Long to = data.getTo();
			    		if(entry!=null) {
			    			log.info("initCall from entry is : " + entry);
			    			SocketIOClient targetClient = server.getClient(entry);
			    			
			    			if(targetClient!=null) {
			    				log.info("initCall from in clientId connected: " + targetClient.isChannelOpen());
			    				VedioMessageInitResponse initResponse = new VedioMessageInitResponse();
			    				   initResponse.setFrom(data.getFrom());
				    			   initResponse.setRoom(data.getRoom());
				    			   initResponse.setCalltype(data.getCalltype());
				    			   initResponse.setFromname(data.getFromname());
				    			   initResponse.setUserCode(data.getUserCode());
				    			   initResponse.setMappedUserCode(data.getMappedUserCode());
				    			   
				    			   log.info("initCall from initResponse "+initResponse.toString());
				    			   targetClient.sendEvent("alreadyengaged",initResponse);
				    			   
				    			  
				    			   List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(to);
				               	
				    			   log.info("Check deviceinfoDetails init size-->"+deviceinfoDetails.size());
				               	
				               	String pushMessage = "Someone has sent you a call";
				               	
				               	if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
				               		
				               		UserDeviceInfo userDeviceInfo = (UserDeviceInfo)deviceinfoDetails.get(0);
				               		  if(userDeviceInfo.getPlatform().equals("android") || userDeviceInfo.getPlatform().equals("ios") || userDeviceInfo.getPlatform().equals("iPhone")) {
				               			//PUSH Notification
				   		            	FCMPushGatewayBusinessObject fCMPushGatewayBusinessObject = new FCMPushGatewayBusinessObject();
				   		            	Notification notification = new Notification();
				   		            	
				   		            	Data data1 = new Data();
				   		            	data1.setSource("AET");
				   		            	data1.setBody(pushMessage);
				   		            	
				   		            	String device_id = userDeviceInfo.getPushtoken();  //device id of target
				   		            	
				   		            	fCMPushGatewayBusinessObject.sendAndroidEngagePushMessage(device_id,notification,data1,to);
				               		 }
			    			}
			    			
			    		}
			    		}*/
					//}else {

				    	
				    	socArrMap.put(data.getFrom(), client.getSessionId());
						//socArrMap.put(data.getTo(), client.getSessionId());
					    callRoomMap.put(data.getRoom(),socArrMap);
				    	
				    	//Map<Long,UUID> userIdArr = (Map<Long,UUID>) userIdToSocketIdMap.get(data.getTo());
				    	UUID entry = (UUID) userIdToSocketIdMap.get(data.getTo());
				    	//if(entry!=null ) {
					    	
					    	
					    	//for (Map.Entry<Long,UUID> entry : userIdArr.entrySet())  {
					    	//for (UUID  entry : userIdArr)  {
					    		//log.info("initCall in clientId: " + entry.toString() + " userId "+data.getTo());
					    		
					    		
					    		if(entry!=null) {
					    			log.info("initCall entry is : " + entry+" toUser "+data.getTo());
					    			UUID entryFrom = (UUID) userIdToSocketIdMap.get(data.getFrom());
					    			log.info("initCall entry is : " + entryFrom+" fromUser "+data.getFrom());
					    			
					    			
					    			//ackSender.sendAckData("IncommingCallNotification");
					                //targetClient.sendEvent("IncommingCallNotification", data);
					    			
					    			SocketIOClient targetClient = server.getClient(entry);
					    			//log.info("initCall within entry not null in clientId: " + entry + " userId "+data.getTo()+" client connected "+targetClient.isChannelOpen());
					    			//log.info("initCall within entry not null 1111 in clientId: " + entry);
					    			//log.info("initCall within entry not null 2222 in clientId connected: " + client.isChannelOpen());
					    			//log.info("initCall within entry not null in clientId: " + entry + " client connected "+client.isChannelOpen());
					    			if(targetClient!=null) {
					    			   VedioMessageInitResponse initResponse = new VedioMessageInitResponse();
					    			   log.info("initCall within entry not null in clientId connected: " + targetClient.isChannelOpen());
					    			   
					    			   initResponse.setFrom(data.getFrom());
					    			   initResponse.setRoom(data.getRoom());
					    			   initResponse.setCalltype(data.getCalltype());
					    			   initResponse.setFromname(data.getFromname());
					    			   initResponse.setUserCode(data.getUserCode());
					    			   initResponse.setMappedUserCode(data.getMappedUserCode());
					    			   
					    			   log.info("initCall initResponse "+initResponse.toString());
					    			   targetClient.sendEvent("IncommingCallNotification",initResponse);
					    			   
					    			   CheckTargetOnline(data.getFrom(),data.getTo(),data.getRoom(),data.getCalltype());
					    			   
					    			   //userIdToSocketIdMap.remove(data.getTo(), entry.toString());
					    			}else {
						            	//Checking target device is ofline then send push
									    CheckTargetOffline(data.getFrom(),data.getTo(),data.getRoom(),data.getCalltype());
						            }
					            }else {
					            	//Checking target device is ofline then send push
								    CheckTargetOffline(data.getFrom(),data.getTo(),data.getRoom(),data.getCalltype());
					            }
					        //}
				    	
				    	
				    	
					       //}else {
					    	 //Checking target device is ofline then send push
						     //CheckTargetOffline(data.getFrom(),data.getTo(),data.getRoom(),data.getCalltype(),userIdArr);
					       //}
				    	 
					//}
				
			
			
	    	
	    	
	    	
			}catch(Exception e) {
				e.printStackTrace();
				log.info("Exception is"+e.getMessage());
			}
	    	
        });
		
		server.addEventListener("calltime", VedioMessage.class, (client, data, ackSender) -> {
			log.info("calltime Data: " + data.toString());
			
			UUID entry = (UUID) userIdToSocketIdMap.get(data.getFrom());
			
			if(entry!=null ) {
				log.info("calltime entry: " + entry);
				
				SocketIOClient targetClient = server.getClient(entry);
				if(targetClient!=null) {
					log.info("calltime is targetClient open from UUID : " + targetClient.isChannelOpen()+" user from :" +data.getFrom());
					
					CalltimeResponse calltimeResponse = new CalltimeResponse();
					
					calltimeResponse.setFrom(data.getFrom());
					calltimeResponse.setTo(data.getTo());
					calltimeResponse.setStarttime(data.getStarttime());
					calltimeResponse.setEndtime(data.getEndtime());
					
					log.info("calltime from calltimeResponse "+calltimeResponse.toString());
					targetClient.sendEvent("calltime",calltimeResponse);
				}
			}
			
           UUID entryto = (UUID) userIdToSocketIdMap.get(data.getTo());
			
			if(entryto!=null ) {
				log.info("calltime entryto: " + entryto);
				
				SocketIOClient targetClient = server.getClient(entryto);
				if(targetClient!=null) {
					log.info("calltime is targetClient open to UUID : " + targetClient.isChannelOpen()+" user to :" +data.getTo());
					
					CalltimeResponse calltimeResponse = new CalltimeResponse();
					
					calltimeResponse.setFrom(data.getFrom());
					calltimeResponse.setTo(data.getTo());
					calltimeResponse.setStarttime(data.getStarttime());
					calltimeResponse.setEndtime(data.getEndtime());
					
					log.info("calltime to calltimeResponse "+calltimeResponse.toString());
					targetClient.sendEvent("calltime",calltimeResponse);
				}
			}
		});
		
		
		server.addEventListener("misesdcall", VedioMessage.class, (client, data, ackSender) -> {
			log.info("misesdcall Data: " + data.toString());
			
			if(data.getFrom()==null) {}
			else {
	            UUID entry = (UUID) userIdToSocketIdMap.get(data.getFrom());
	            
	            if(entry!=null ) {
	            	log.info("misesdcall entry: " + entry);
	            	//for (UUID  entry : userIdArr) {
	            		SocketIOClient targetClient = server.getClient(entry);
	            		if(targetClient!=null) {
	            			log.info("misesdcall is targetClient open from UUID : " + targetClient.isChannelOpen()+" user :" +data.getFrom());
	            			MissedCallResponse missResponse = new MissedCallResponse();
	            			missResponse.setCall(data.getCall());
	            			
	            			targetClient.sendEvent("misesdcall",missResponse);
	            			//userIdArr.remove(entry);
	            			//userIdToSocketIdMap.remove(data.getFrom(), userIdArr);
	            		}
	            	//}
	            }
			}
			
			if(data.getTo()==null) {}
			else {
            UUID entryTo = (UUID) userIdToSocketIdMap.get(data.getTo());
            
            if(entryTo!=null ) {
            	log.info("misesdcall entryTo: " + entryTo);
            	//for (UUID  entry : userIdArrTo) {
            		SocketIOClient targetClient = server.getClient(entryTo);
            		if(targetClient!=null) {
            			log.info("misesdcall is targetClient open to UUID : " + targetClient.isChannelOpen()+ " user :"+data.getTo());
            			MissedCallResponse missResponse = new MissedCallResponse();
            			missResponse.setCall(data.getCall());
            			
            			targetClient.sendEvent("misesdcall",missResponse);
            			
            			
            			
            			List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(data.getTo());
	    				log.info("Check deviceinfoDetails size for Missed call-->"+deviceinfoDetails.size());
	    				String pushMessage = data.getTo() +" is missed the call";
	    				
	    				if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
	                		
	                		UserDeviceInfo userDeviceInfo = (UserDeviceInfo)deviceinfoDetails.get(0);
	                		  if(userDeviceInfo.getPlatform().equals("android") || userDeviceInfo.getPlatform().equals("ios") || userDeviceInfo.getPlatform().equals("iPhone")) {
	                			//PUSH Notification
	    		            	FCMPushGatewayBusinessObject fCMPushGatewayBusinessObject = new FCMPushGatewayBusinessObject();
	    		            	Notification notification = new Notification();
	    		            	
	    		            	PushData data1 = new PushData();
	    		            	data1.setSource("AET");
	    		            	data1.setBody(pushMessage);
	    		            	
	    		            	data1.setRooms(data.getRoom());
	    		            	data1.setSourceUserId(data.getFrom());
	    		            	data1.setTargetUserId(data.getTo());
	    		            	
	    		            	String device_id = userDeviceInfo.getPushtoken();  //device id of target
	    		            	
	    		            	fCMPushGatewayBusinessObject.sendAndroidMissedCallPushMessage(device_id,notification,data1,data.getTo());
	                		 }
	                	}
	    				
	    				
	    				String pushMessage1 = "Someone has sent you a missed call";
	                    
	                    if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
	                      //for(UserDeviceInfo userDeviceInfo : deviceinfoDetails) {
	                      UserDeviceInfo userDeviceInfo = (UserDeviceInfo)deviceinfoDetails.get(0);
	                        if(userDeviceInfo.getPlatform().equals("android") || userDeviceInfo.getPlatform().equals("ios") || userDeviceInfo.getPlatform().equals("iPhone")) {
	                        //PUSH Notification
	                        FCMPushGatewayBusinessObject fCMPushGatewayBusinessObject = new FCMPushGatewayBusinessObject();
	                        Notification notification = new Notification();
	                        
	                        Data data1 = new Data();
	                        data1.setSource("AET");
	                        data1.setBody(pushMessage1);
	                        
	                        String device_id = userDeviceInfo.getPushtoken();  //device id of target
	                        
	                        fCMPushGatewayBusinessObject.sendAndroidMissedCallTextPushMessage(device_id,notification,data1,data.getTo());
	                       }/*else {
	                        //WEB Push Notification
	                        webPushApplicationService.sendNotifications(pushMessage);
	                       }*/
	                      //}
	                    }
            			//userIdArrTo.remove(entry);
            			//userIdToSocketIdMap.remove(data.getTo(), userIdArrTo);
            		}
            	//}
            }
            
		  }
			
			if(data.getFrom()==null) {}
			else {
				boolean statusFromend = chatUsersService.getUsersMappedMessegeListForMissedCall(data.getFrom(),data.getTo(),data.getRoom());
				boolean statusToend = chatUsersService.getUsersMappedMessegeListForMissedCall(data.getTo(),data.getFrom(),data.getRoom());
				
				ChatMessageDetails chatMessageDetails = new ChatMessageDetails();
				
				if(statusFromend == false && statusToend == false) {
					log.info("Check for save in Missed call statusFromend -->"+ statusFromend + " statusToend -->"+statusToend);
					    chatMessageDetails.setUserid(data.getInitiateCallUser());
						
					//chatMessageDetails.setUserid(data.getFrom());
					
					if(data.getInitiateCallUser().equals(data.getFrom()) || data.getInitiateCallUser()==data.getFrom())
					    chatMessageDetails.setMappeduserid(data.getTo());
					else
						chatMessageDetails.setMappeduserid(data.getFrom());
					
					    chatMessageDetails.setIsreadmsg(1);
					    chatMessageDetails.setCreatedon(System.currentTimeMillis());
					    chatMessageDetails.setCalltype(data.getCalltype());
					    chatMessageDetails.setCallduration(data.getCall());
					    chatMessageDetails.setMessagebody(data.getCall());
					    chatMessageDetails.setDevplatform(data.getDevplatform());
					    chatMessageDetails.setRoomno(data.getRoom());
					
					
					
					
					chatUsersService.saveChatMessegeDetails(chatMessageDetails);
				}	
			}
				
		});
		
		
		server.addEventListener("getCallDetails", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("getCallDetails call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("getCallDetails Data: " + data.toString());
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("getCallDetails soctempArr size 1 is : " + soctempArr.size());
			
			if(soctempArr!=null && soctempArr.size()>0) {
			//soctempArr.put(data.getTo(), client.getSessionId());
			soctempArr.put(data.getTo(), (UUID) userIdToSocketIdMap.get(data.getTo()));
	    	callRoomMap.put(data.getRoom(),soctempArr);
	    	
	    	log.info("getCallDetails soctempArr size 2 is : " + soctempArr.size());
	    	
	    	if(soctempArr!=null && soctempArr.size()>0) {
	    	
		    	//for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		//log.info("getCallDetails in userId: " + entry.getKey() + " clientId "+entry.getValue());
		    		
		    		//if(data.getTo()!=entry.getKey() || !data.getTo().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			UUID tClientId = soctempArr.get(data.getFrom());
		    			SocketIOClient targetClient = server.getClient(tClientId);
		    			
		    			
		    			
		    			log.info("getCallDetails in from userId: " + data.getFrom() + " clientId "+tClientId);
		    			
		    			
		    			if(targetClient!=null) {
		    				log.info("getCallDetails is targetClient open: " + targetClient.isChannelOpen());
		    				targetClient.sendEvent("userInRoom");
		    			}
		    		//}
		    	//}
		    	/*for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		log.info("getCallDetails socketid: " +data.getSocketid());
		    		UUID targetClientId = entry.getValue();
		    		
		    		
		    		if(StringUtils.isNoneEmpty(data.getSocketid())) {
		    		 if(data.getTo().equals(entry.getKey()) || data.getTo()==entry.getKey()) {
		    		  if(!targetClientId.toString().equals(data.getSocketid())) {
		    			log.info("getCallDetails in to userId: " + entry.getKey() + " targetClientId "+targetClientId);
		    			SocketIOClient targetClientt = server.getClient(targetClientId);
		    			
		    			if(targetClientt!=null) {
		    				log.info("getCallDetails is targetClientt open: " + targetClientt.isChannelOpen());
		    				CallAlreadyReceivedResponse res = new CallAlreadyReceivedResponse();
		    				res.setCallalreadyreceived("callalreadyreceived");
		    				res.setSocketid(data.getSocketid());
		    				
		    				log.info("getCallDetails CallAlreadyReceivedResponse "+res.toString());
		    				
		    				targetClientt.sendEvent("callalreadyreceived",res);
		    			}
		    		 }
		    		}
		    	  }
		    	}*/
	    	
	    	}
		  }
	    	
	    });
		
		
		server.addEventListener("startCall", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("startCall call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("startCall Data: " + data.toString());
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("startCall soctempArr size: " + soctempArr.size());
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		
		    		
		    		if(data.getFrom()!=entry.getKey() || !data.getFrom().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			UUID tclientId = soctempArr.get(data.getTo());
		    			SocketIOClient targetClient = server.getClient(tclientId);
		    			
		    			log.info("startCall in to userId: " + data.getTo() + " clientId "+tclientId);
		    			
		    			if(targetClient!=null) {
		    				log.info("startCall is targetClient open: " + targetClient.isChannelOpen());
		    				VedioMessageStartResponse startResponse = new VedioMessageStartResponse();
		    				startResponse.setFrom(data.getFrom());
		    				startResponse.setOffer(data.getOffer());
		    				
		    				log.info("startCall startResponse "+startResponse.toString());
		    				
		    				targetClient.sendEvent("incommingcall",startResponse);
		    				break;
		    			}
		    		}
		    	}
			}
			
		});
		
		
		 server.addEventListener("acceptCall", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("acceptCall call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("acceptCall Data: " + data.toString());
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("acceptCall soctempArr size: " + soctempArr.size());
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		
		    		
		    		if(data.getFrom()!=entry.getKey() || !data.getFrom().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			UUID tclientId = soctempArr.get(data.getTo());
		    			SocketIOClient targetClient = server.getClient(tclientId);
		    			
		    			log.info("acceptCall in userId: " + data.getTo() + " clientId "+tclientId);
		    			
		    			if(targetClient!=null) {
		    				log.info("acceptCall is targetClient open: " + targetClient.isChannelOpen());
		    				VedioMessageAcceptResponse acceptResponse = new VedioMessageAcceptResponse();
		    				acceptResponse.setFrom(data.getFrom());
		    				acceptResponse.setAns(data.getAns());
		    				
		    				log.info("acceptCall acceptResponse "+acceptResponse.toString());
		    				
		    				targetClient.sendEvent("callAccepted",acceptResponse);
		    				
		    				List<ChatAudioVedioDetails> list = chatUsersService.getAudioVedioDetailsByUserId( data.getTo(), data.getFrom());
		 	    		    if(list!=null && list.size()>0) {
		 	    			   ChatAudioVedioDetails chatAudioVedioDetails = (ChatAudioVedioDetails) list.get(0);
		 	    			   
		 	    			   chatAudioVedioDetails.setStarttime(System.currentTimeMillis());
		 	    			   chatAudioVedioDetails.setCallduration("duration");
		 	    			   
		 	    			   chatUsersService.saveAudiovedioMasters(chatAudioVedioDetails);
		 	    			   
		 	    			  List<ChatMessageDetails> mlist = chatUsersService.getUsersMappedMessegeList(data.getTo(), data.getFrom());
		 	    			  if(mlist!=null && mlist.size()>0) {
		 	    				 ChatMessageDetails chatMessageDetails = (ChatMessageDetails) mlist.get(0);
		 	    				 
		 	    				chatMessageDetails.setDevplatform(data.getDevplatform());
		 	    				chatMessageDetails.setCallduration("duration");
		 	    				
		 	    				chatUsersService.saveChatMessegeDetails(chatMessageDetails);
		 	    			  }
		 	    		   }
		 	    		    break;
		    			}
		    		}
		    	}
			}
	    	
	    });
		 
		 
		server.addEventListener("checkUserStatus", VedioMessage.class, (client, data, ackSender) -> {
			log.info("checkUserStatus Data: " + data.toString());
			
			
			
			
				UUID clientIdfrom = (UUID) userIdToSocketIdMap.get(data.getFrom());
				log.info("checkUserStatus clientIdfrom: "+clientIdfrom);
				
				Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
				if(soctempArr!=null && soctempArr.size()>0) {
				
				UUID clientIdto = soctempArr.get(data.getTo());
				
				//if(clientIdfrom!=null) {
					log.info("checkUserStatus in touserId: " + data.getTo() + " clientIdto "+clientIdto+ " fromUserId: "+data.getFrom()+ " clientIdfrom: "+clientIdfrom);
					
					SocketIOClient targetClientfrom = server.getClient(clientIdfrom);
					if(targetClientfrom!=null) {
						log.info("checkUserStatus is targetClientfrom open: " + targetClientfrom.isChannelOpen());
						SocketIOClient targetClientto = server.getClient(clientIdto);
						
						CheckUserStatusCheckResponse status = new CheckUserStatusCheckResponse();
						
						if(targetClientto!=null) {
							log.info("checkUserStatus is targetClientto open in IF: " + targetClientto.isChannelOpen());
							
							status.setFrom(data.getFrom());
							status.setTo(data.getTo());
							status.setRoom(data.getRoom());
							status.setTargetuserStatus("true");
							
							log.info("checkUserStatus Response in IF "+status.toString());
							targetClientfrom.sendEvent("checkUserStatusResponse",status);
						}else {
							log.info("checkUserStatus is targetClientto open in ELSE: " + targetClientto.isChannelOpen());
							
							status.setFrom(data.getFrom());
							status.setTo(data.getTo());
							status.setRoom(data.getRoom());
							status.setTargetuserStatus("false");
							
							log.info("checkUserStatus Response in ELSE "+status.toString());
							targetClientfrom.sendEvent("checkUserStatusResponse",status);
						}
					//}
				}
			}else {
				log.info(" fromUserId: "+data.getFrom()+ " clientIdfrom: "+clientIdfrom);
				SocketIOClient targetClientfrom = server.getClient(clientIdfrom);
				
				if(targetClientfrom!=null) {
					log.info("checkUserStatus is targetClientfrom open in : " + targetClientfrom.isChannelOpen());
					CheckUserStatusCheckResponse status = new CheckUserStatusCheckResponse();
					
					status.setFrom(data.getFrom());
					status.setTo(data.getTo());
					status.setRoom(data.getRoom());
					status.setTargetuserStatus("false");
					
					log.info("checkUserStatus Response in  "+status.toString());
					targetClientfrom.sendEvent("checkUserStatusResponse",status);
				}
			}
		});
		
		
		server.addEventListener("negotiationneeded", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("negotiationNeeded call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("negotiationNeeded Data: " + data.toString());
			
			
			
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("negotiationNeeded soctempArr size: " + soctempArr.size());
			UUID clientIdto = soctempArr.get(data.getTo());
			log.info("negotiationNeeded 11 in touserId: " + data.getTo() + " clientIdto "+clientIdto);
			if(clientIdto==null) {
				//soctempArr.put(data.getTo(), client.getSessionId());
				if(userIdToSocketIdMap.get(data.getTo())!=null)
				 soctempArr.put(data.getTo(), (UUID) userIdToSocketIdMap.get(data.getTo()));
			}
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				//for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		//log.info("negotiationNeeded in userId: " + entry.getKey() + " clientId "+entry.getValue());
		    		
		    		//if(data.getFrom()!=entry.getKey() || !data.getFrom().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			clientIdto = soctempArr.get(data.getTo());
		    			if(clientIdto!=null) {
			    			log.info("negotiationNeeded in touserId: " + data.getTo() + " clientIdto "+clientIdto);
			    			SocketIOClient targetClient = server.getClient(clientIdto);
			    			
			    			if(targetClient!=null) {
			    				log.info("negotiationNeeded is targetClient open: " + targetClient.isChannelOpen());
			    				
			    				VedioMessageStartResponse startResponse = new VedioMessageStartResponse();
			    				startResponse.setFrom(data.getFrom());
			    				startResponse.setOffer(data.getOffer());
			    				
			    				log.info("negotiationNeeded Response "+startResponse.toString());
			    				
			    				targetClient.sendEvent("negotiationneeded",startResponse);
			    			}
		    			}
		    		//}
		    	//}
			}
	 });
		
		
	server.addEventListener("negotiationDone", VedioMessage.class, (SocketIOClient, data, ackSender) -> {
            // Handle startcall event
			//log.info("negotiationDone call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("negotiationDone Data: " + data.toString());
			
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("negotiationDone soctempArr size: " + soctempArr.size());
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				
				if(data.getFrom()!=null && data.getTo()!=null) {
				
					//for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
			    		//log.info("negotiationDone in userId: " + entry.getKey() + " clientId "+entry.getValue());
			    		
			    		//if(data.getFrom()!=entry.getKey() || !data.getFrom().equals(entry.getKey())) {
			    			//SocketIOClient targetClient = server.getClient(entry.getValue());
			    			UUID clientIdto = soctempArr.get(data.getTo());
			    			log.info("negotiationDone in touserId: " + data.getTo() + " clientIdto "+clientIdto);
			    			SocketIOClient targetClient = server.getClient(clientIdto);
			    			
			    			if(targetClient!=null) {
			    				log.info("negotiationDone is targetClient open: " + targetClient.isChannelOpen());
			    				VedioMessageNegotiationResponse doneResponse = new VedioMessageNegotiationResponse();
			    				doneResponse.setFrom(data.getFrom());
			    				doneResponse.setAns(data.getAns());
			    				
			    				log.info("negotiationDone Response "+doneResponse.toString());
			    				
			    				targetClient.sendEvent("negotiationFinal",doneResponse);
			    			}
			    		//}
			    	//}
				}
			}
			
		});
		
		server.addEventListener("iceCandidate", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("iceCandidate call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("iceCandidate Data: " + data.toString());
			
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("iceCandidate soctempArr size: " + soctempArr.size());
			
			UUID clientIdto = soctempArr.get(data.getTo());
			if(clientIdto==null) {
				//soctempArr.put(data.getTo(), client.getSessionId());
				if(userIdToSocketIdMap.get(data.getTo())!=null)
				  soctempArr.put(data.getTo(), (UUID) userIdToSocketIdMap.get(data.getTo()));
			}
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		log.info("iceCandidate in userId: " + entry.getKey() + " clientId "+entry.getValue());
		    		
		    		if(data.getFrom()!=entry.getKey() || !data.getFrom().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			
		    			clientIdto = soctempArr.get(data.getTo());
		    			if(clientIdto!=null) {
		    			log.info("iceCandidate in touserId: " + data.getTo() + " clientIdto "+clientIdto);
		    			SocketIOClient targetClient = server.getClient(clientIdto);
		    			
		    			if(targetClient!=null) {
		    				log.info("iceCandidate is targetClient open: " + targetClient.isChannelOpen());
		    				VedioMessageIceCandidateResponse iceResponse = new VedioMessageIceCandidateResponse();
		    				iceResponse.setFrom(data.getFrom());
		    				iceResponse.setCandidate(data.getCandidate());
		    				
		    				log.info("iceCandidate iceResponse "+iceResponse.toString());
		    				
		    				targetClient.sendEvent("iceCandidate",iceResponse);
		    			}
		    		  }
		    		}
		    	}
			}
		 });
		
		
		server.addEventListener("enblevideo", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("iceCandidate call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("enblevideo Data: " + data.toString());
			
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("enblevideo soctempArr size: " + soctempArr.size());
			
			UUID clientIdto = soctempArr.get(data.getTo());
			if(clientIdto==null) {
				//soctempArr.put(data.getTo(), client.getSessionId());
				if(userIdToSocketIdMap.get(data.getTo())!=null)
				 soctempArr.put(data.getTo(), (UUID) userIdToSocketIdMap.get(data.getTo()));
			}
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		log.info("enblevideo in userId: " + entry.getKey() + " clientId "+entry.getValue());
		    		
		    		if(data.getTo()==entry.getKey() || data.getTo().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			
		    			clientIdto = soctempArr.get(data.getTo());
		    			log.info("enblevideo in touserId: " + data.getTo() + " clientIdto "+clientIdto);
		    			SocketIOClient targetClient = server.getClient(clientIdto);
		    			
		    			if(targetClient!=null) {
		    				log.info("enblevideo is targetClient open: " + targetClient.isChannelOpen());
		    				
		    				
		    				VedioResponseMessage vedioRes = new VedioResponseMessage();
		    				vedioRes.setVideo(data.getVideo());
		    				
		    				targetClient.sendEvent("enblevideo",vedioRes);
		    			}
		    		}
		    	}
			}
		 });
		
		
		server.addEventListener("disablevideo", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("iceCandidate call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("enblevideo Data: " + data.toString());
			
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("disablevideo soctempArr size: " + soctempArr.size());
			
			UUID clientIdto = soctempArr.get(data.getTo());
			if(clientIdto==null) {
				//soctempArr.put(data.getTo(), client.getSessionId());
				if(userIdToSocketIdMap.get(data.getTo())!=null)
				 soctempArr.put(data.getTo(), (UUID) userIdToSocketIdMap.get(data.getTo()));
			}
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		log.info("disablevideo in userId: " + entry.getKey() + " clientId "+entry.getValue());
		    		
		    		if(data.getTo()==entry.getKey() || data.getTo().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			
		    			clientIdto = soctempArr.get(data.getTo());
		    			log.info("disablevideo in touserId: " + data.getTo() + " clientIdto "+clientIdto);
		    			SocketIOClient targetClient = server.getClient(clientIdto);
		    			
		    			if(targetClient!=null) {
		    				log.info("disablevideo is targetClient open: " + targetClient.isChannelOpen());
		    				
		    				VedioResponseMessage vedioRes = new VedioResponseMessage();
		    				vedioRes.setVideo(data.getVideo());
		    				
		    				targetClient.sendEvent("disablevideo",vedioRes);
		    			}
		    		}
		    	}
			}
		 });
		
		
		
		server.addEventListener("enbleaudio", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("iceCandidate call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("enbleaudio Data: " + data.toString());
			
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("enbleaudio soctempArr size: " + soctempArr.size());
			
			UUID clientIdto = soctempArr.get(data.getTo());
			if(clientIdto==null) {
				//soctempArr.put(data.getTo(), client.getSessionId());
				if(userIdToSocketIdMap.get(data.getTo())!=null)
				 soctempArr.put(data.getTo(), (UUID) userIdToSocketIdMap.get(data.getTo()));
			}
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		log.info("enbleaudio in userId: " + entry.getKey() + " clientId "+entry.getValue());
		    		
		    		if(data.getTo()==entry.getKey() || data.getTo().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			
		    			clientIdto = soctempArr.get(data.getTo());
		    			log.info("enbleaudio in touserId: " + data.getTo() + " clientIdto "+clientIdto);
		    			SocketIOClient targetClient = server.getClient(clientIdto);
		    			
		    			if(targetClient!=null) {
		    				log.info("enbleaudio is targetClient open: " + targetClient.isChannelOpen()+ " to User :"+data.getTo());
		    				AudioVedioResponseMessage au = new AudioVedioResponseMessage();
		    				au.setAudio(data.getAudio());
		    				
		    				targetClient.sendEvent("enbleaudio",au);
		    			}
		    		}
		    	}
			}
		 });
		
		
		server.addEventListener("disableaudio", VedioMessage.class, (client, data, ackSender) -> {
            // Handle startcall event
			//log.info("iceCandidate call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("disableaudio Data: " + data.toString());
			
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("disableaudio soctempArr size: " + soctempArr.size());
			
			UUID clientIdto = soctempArr.get(data.getTo());
			if(clientIdto==null) {
				//soctempArr.put(data.getTo(), client.getSessionId());
				if(userIdToSocketIdMap.get(data.getTo())!=null)
				 soctempArr.put(data.getTo(), (UUID) userIdToSocketIdMap.get(data.getTo()));
			}
	    	
			if(soctempArr!=null && soctempArr.size()>0) {
				for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
		    		log.info("disableaudio in userId: " + entry.getKey() + " clientId "+entry.getValue());
		    		
		    		if(data.getTo()==entry.getKey() || data.getTo().equals(entry.getKey())) {
		    			//SocketIOClient targetClient = server.getClient(entry.getValue());
		    			
		    			clientIdto = soctempArr.get(data.getTo());
		    			log.info("disableaudio in touserId: " + data.getTo() + " clientIdto "+clientIdto);
		    			SocketIOClient targetClient = server.getClient(clientIdto);
		    			
		    			if(targetClient!=null) {
		    				log.info("disableaudio is targetClient open: " + targetClient.isChannelOpen()+ " to User :"+data.getTo());
		    				
		    				AudioVedioResponseMessage au = new AudioVedioResponseMessage();
		    				au.setAudio(data.getAudio());	    				
		    				targetClient.sendEvent("disableaudio",au);
		    			}
		    		}
		    	}
			}
		 });
		
		
		
		server.addEventListener("endCall", VedioMessage.class, (SocketIOClient, data, ackSender) -> {
            // Handle startcall event
			//log.info("endCall call received from client: " + SocketIOClient.getSessionId().toString());
        	//logger.info("Data: " + data.toString());
			log.info("endCall Data: " + data.toString());
			
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			//log.info("endCall soctempArr size: " + soctempArr.size());
			
			
			
			if(soctempArr!=null && soctempArr.size()>0) {
				
				//for (Map.Entry<Long,UUID> entry : soctempArr.entrySet()) {
					//log.info("endCall in userId: " + entry.getKey() + " clientId "+entry.getValue());
					
					//if(data.getFrom()==entry.getKey() || data.getFrom().equals(entry.getKey())) {
				     if(data.getFrom()==null) {}
				     else {
						UUID clientIdfrom = soctempArr.get(data.getFrom());
						
						log.info("endCall is clientIdfrom open in From: " +clientIdfrom + " user :"+data.getFrom());
						if(clientIdfrom!=null) {
							SocketIOClient targetClient = server.getClient(clientIdfrom);
							
							if(targetClient!=null) {
			    				log.info("endCall is targetClient open in From: " + targetClient.isChannelOpen()+" user "+data.getFrom()+" clientIdfrom "+clientIdfrom);
			    				//String from = "own";
			    				EndCallResponse endCallResponse = new EndCallResponse();
			    				endCallResponse.setFrom(data.getFrom());
			    				endCallResponse.setTo(data.getTo());
			    				
			    				log.info("endCall Response "+endCallResponse.toString());
			    				
			    				targetClient.sendEvent("endCall",endCallResponse);
			    				
			    				userIdToSocketIdMap.remove(data.getFrom(), clientIdfrom);
			    				
			    				/*if(callRoomMap.containsKey(data.getRoom())) {
			    					callRoomMap.remove(data.getRoom());
			    				}
			    				
			    				if(userIdToSocketIdMap.containsKey(data.getFrom())) {
			    				     userIdToSocketIdMap.remove(data.getFrom(),entry.getValue());
			    				}*/
			    			}
						}
				     }
				     if(data.getTo()==null) {}
				     else {
						UUID clientIdto = soctempArr.get(data.getTo());
						//if(clientIdto!=null) {
						//log.info("endCall is clientIdto open in To: " +clientIdto + " user :"+data.getTo());
						
						List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(data.getTo());
	    				log.info("Check deviceinfoDetails size for end call-->"+deviceinfoDetails.size());
	    				String pushMessage = data.getTo() +" is ended the call";
	    				
	    				if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
	                		
	                		UserDeviceInfo userDeviceInfo = (UserDeviceInfo)deviceinfoDetails.get(0);
	                		  if(userDeviceInfo.getPlatform().equals("android") || userDeviceInfo.getPlatform().equals("ios") || userDeviceInfo.getPlatform().equals("iPhone")) {
	                			//PUSH Notification
	    		            	FCMPushGatewayBusinessObject fCMPushGatewayBusinessObject = new FCMPushGatewayBusinessObject();
	    		            	Notification notification = new Notification();
	    		            	
	    		            	PushData data1 = new PushData();
	    		            	data1.setSource("AET");
	    		            	data1.setBody(pushMessage);
	    		            	
	    		            	data1.setRooms(data.getRoom());
	    		            	data1.setSourceUserId(data.getFrom());
	    		            	data1.setTargetUserId(data.getTo());
	    		            	
	    		            	String device_id = userDeviceInfo.getPushtoken();  //device id of target
	    		            	
	    		            	fCMPushGatewayBusinessObject.sendAndroidEndPushMessage1(device_id,notification,data1,data.getTo());
	                		 }
	                	}
						
	    				if(clientIdto!=null) {
						SocketIOClient targetClientto = server.getClient(clientIdto);
						
						if(targetClientto!=null) {
		    				log.info("endCall is targetClient open in To: " + targetClientto.isChannelOpen()+" user "+data.getTo()+" clientIdto "+clientIdto);
		    				//String from = Long.toString(data.getTo());
		    				
		    				
		    				EndCallResponse endCallResponse = new EndCallResponse();
		    				endCallResponse.setFrom(data.getFrom());
		    				endCallResponse.setTo(data.getTo());
		    				
		    				log.info("endCall Response "+endCallResponse.toString());
		    				
		    				targetClientto.sendEvent("endCall",endCallResponse);
		    				
		    				
		    			}
						
						}
				     }
						
						/*}else {
						UUID clientIdto = soctempArr.get(data.getTo());
						SocketIOClient targetClient = server.getClient(clientIdto);
						
						if(targetClient!=null) {
		    				log.info("endCall is targetClient open in ELSE: " + targetClient.isChannelOpen());
		    				String from = Long.toString(data.getFrom());
		    				targetClient.sendEvent("endCall",from);
		    				
		    				
		    			}
						
						
					}*/
					
					  List<ChatAudioVedioDetails> list = chatUsersService.getAudioVedioDetailsByUserId(data.getFrom(), data.getTo());
		    		   if(list!=null && list.size()>0) {
		    			   ChatAudioVedioDetails chatAudioVedioDetails = (ChatAudioVedioDetails) list.get(0);
		    			   
		    			   long callendtime = System.currentTimeMillis();
		    			   chatAudioVedioDetails.setCallendtime(callendtime);
		    			   
		    			   
		    			   /*Long startTime = chatAudioVedioDetails.getStarttime();
		    			   
		    			   String lengthofduration = null;
		    			   long diff = 0l;
		    			   
		    			   if(startTime!=null && startTime>0) {
		    				    diff = callendtime - startTime;
		    			   }
		    			   
		    			   long diffSeconds = diff / 1000 % 60;
		    		       long diffMinutes = diff / (60 * 1000) % 60;
		    		       long diffHours = diff / (60 * 60 * 1000);
		    		       
		    		       log.info("Seconds difference: " + diffSeconds);
		    		       log.info("Minutes difference: " + diffMinutes);
		    		       log.info("Hours difference: " + diffHours);
		    		       
		    		       if(diffMinutes>0 && diffSeconds==0) {
		    		    	   lengthofduration = diffMinutes + "minutes";
		    		       }
		    		       if(diffMinutes>0 && diffSeconds>0) {
		    		    	   lengthofduration = diffMinutes + "minutes" + diffSeconds + "seconds";
		    		       }
		    		       if(diffMinutes==0 && diffSeconds>0) {
		    		    	   lengthofduration = diffMinutes + "seconds";
		    		       }*/
		    		       chatAudioVedioDetails.setLengthofduration(data.getCallduration());
		    		       
		    		       
		    			   
		    			   chatUsersService.saveAudiovedioMasters(chatAudioVedioDetails);
		    		   }
		    		   
		    		   List<ChatAudioVedioDetails> listto = chatUsersService.getAudioVedioDetailsByUserId(data.getTo(),data.getFrom());
		    		   if(listto!=null && listto.size()>0) {
		    			   ChatAudioVedioDetails chatAudioVedioDetails = (ChatAudioVedioDetails) listto.get(0);
		    			   
		    			   long callendtime = System.currentTimeMillis();
		    			   chatAudioVedioDetails.setCallendtime(callendtime);
		    			   
		    			   
		    			   /*Long startTime = chatAudioVedioDetails.getStarttime();
		    			   
		    			   String lengthofduration = null;
		    			   long diff = 0l;
		    			   
		    			   if(startTime!=null && startTime>0) {
		    				    diff = callendtime - startTime;
		    			   }
		    			   
		    			   long diffSeconds = diff / 1000 % 60;
		    		       long diffMinutes = diff / (60 * 1000) % 60;
		    		       long diffHours = diff / (60 * 60 * 1000);
		    		       
		    		       log.info("Seconds difference: " + diffSeconds);
		    		       log.info("Minutes difference: " + diffMinutes);
		    		       log.info("Hours difference: " + diffHours);
		    		       
		    		       if(diffMinutes>0 && diffSeconds==0) {
		    		    	   lengthofduration = diffMinutes + "minutes";
		    		       }
		    		       if(diffMinutes>0 && diffSeconds>0) {
		    		    	   lengthofduration = diffMinutes + "minutes" + diffSeconds + "seconds";
		    		       }
		    		       if(diffMinutes==0 && diffSeconds>0) {
		    		    	   lengthofduration = diffMinutes + "seconds";
		    		       }*/
		    		       chatAudioVedioDetails.setLengthofduration(data.getCallduration());
		    		       
		    		       
		    			   
		    			   chatUsersService.saveAudiovedioMasters(chatAudioVedioDetails);
		    		   }
				//}
			}
			
			
	    });
		
		server.addEventListener("clearRoomandSockets", VedioMessage.class, (SocketIOClient, data, ackSender) -> {
            log.info("clearRoomandSockets Data: " + data.toString());
			
            UUID userIdArr = (UUID) userIdToSocketIdMap.get(data.getFrom());
            
            if(userIdArr!=null ) {
            	log.info("clearRoomandSockets userIdArr : " + userIdArr);
            	/*for (UUID  entry : userIdArr) {
            		//SocketIOClient targetClient = server.getClient(entry);
            		if(entry!=null) {
            			//log.info("clearRoomandSockets is targetClient open from : " + targetClient.isChannelOpen());
            			userIdArr.remove(entry);
            			userIdToSocketIdMap.remove(data.getFrom(), userIdArr);
            		}
            	}*/
            	//userIdToSocketIdMap.remove(data.getFrom(), userIdArr);
            }
            
          // List<UUID> userIdArrTo = (List<UUID>) userIdToSocketIdMap.get(data.getTo());
            
          //  if(userIdArrTo!=null && userIdArrTo.size()>0) {
            //	log.info("clearRoomandSockets userIdArrTo size: " + userIdArrTo.size());
            	/*for (UUID  entry : userIdArrTo) {
            		//SocketIOClient targetClient = server.getClient(entry);
            		if(entry!=null) {
            			//log.info("clearRoomandSockets is targetClient open to : " + targetClient.isChannelOpen());
            			userIdArr.remove(entry);
            			userIdToSocketIdMap.remove(data.getTo(), userIdArr);
            		}
            	}*/
            //	userIdToSocketIdMap.remove(data.getTo(), userIdArrTo);
          //  }
            
			Map<Long,UUID>  soctempArr = (Map<Long,UUID>) callRoomMap.get(data.getRoom());
			
			if(soctempArr!=null && soctempArr.size()>0) {
				log.info("clearRoomandSockets soctempArr size: " + callRoomMap.size());
				callRoomMap.remove(data.getRoom());
			}
		});
		
		return server;
	}
	
	public void CheckTargetOnline(Long from,Long to,Long room,String callType) {
		//for (UUID  entry : userIdArr) {
		//if(entry!=null) {
			//SocketIOClient targetClient = server.getClient(entry);
			//if(targetClient==null) {
				List<ChatUsersMaster> userList = chatUsersService.getUsersMappedList(from);
	            List<ChatUsersMaster> mappedUserList = chatUsersService.getUsersMappedList(to);
	             
    			List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(to);
    			String pushMessage = null;
    			
    			pushMessage = "Someone has sent you a "+ callType +" call";
    			
    			
    			log.info("Check deviceinfoDetails size in CheckTargetOnline init call-->"+deviceinfoDetails.size());
    			if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
    				UserDeviceInfo userDeviceInfo = (UserDeviceInfo)deviceinfoDetails.get(0);
            		  if(userDeviceInfo.getPlatform().equals("android") || userDeviceInfo.getPlatform().equals("ios") || userDeviceInfo.getPlatform().equals("iPhone")) {
            			//PUSH Notification
		            	FCMPushGatewayBusinessObject fCMPushGatewayBusinessObject = new FCMPushGatewayBusinessObject();
		            	Notification notification = new Notification();
		            	
		            	PushData data1 = new PushData();
		            	data1.setSource("AET");
		            	data1.setBody(pushMessage);
		            	
		            	ChatUsersMaster fromChatuser = (ChatUsersMaster)userList.get(0);
		            	ChatUsersMaster toChatuser = (ChatUsersMaster)mappedUserList.get(0);
		            	
		            	List<Action> actionlist = new ArrayList<Action>();
		            	
		            	data1.setImage(fromChatuser.getUserphotoimageurl());
		            	data1.setName(fromChatuser.getUsername());
		            	data1.setRoomno(from);
		            	data1.setRooms(room);
		            	data1.setTargetUserId(toChatuser.getUserid());
		            	data1.setMappedUserCode(toChatuser.getUsercode());
		            	data1.setUserCode(fromChatuser.getUsercode());
		            	data1.setCallType(callType);
		            	data1.setSourceUserId(from);
		            	
		            	Action action = new Action();
		            	Action action1 = new Action();
		            	
		            	action.setId("1");
		            	action.setTitle("Accept");
		            	action.setLink("aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL");
		            	action.setRoomno(from.toString());
		            	action.setCallType(callType);
		            	action.setMappedUserCode(toChatuser.getUsercode());
		            	action.setUserCode(fromChatuser.getUsercode());
		            	action.setType("call");
		            	action.setActionColor("#33FF57");
		            	action.setRooms(room.toString());
		            	
		            	actionlist.add(action);
		            	
		            	action1.setId("2");
		            	action1.setTitle("Reject");
		            	action1.setLink(null);
		            	action1.setRoomno(null);
		            	action1.setCallType(null);
		            	action1.setMappedUserCode(null);
		            	action1.setUserCode(null);
		            	action1.setType("call");
		            	action1.setActionColor("#FF5733");
		            	
		            	actionlist.add(action1);
		            	
		            	data1.setActionlist(actionlist);
		            	
		            	String device_id = userDeviceInfo.getPushtoken();  //device id of target
		            	
		            	fCMPushGatewayBusinessObject.sendAndroidCampaignVedioOnlinePushMessage(device_id,notification,data1,userList.get(0).getUserid());
            		 }
    			}
			//}
		//}
	//}

		
	}
	
	//@Async
	public void CheckTargetOffline(Long from,Long to,Long room,String callType) {
		//for (UUID  entry : userIdArr) {
    		//if(entry!=null) {
    			//SocketIOClient targetClient = server.getClient(entry);
    			//if(targetClient==null) {
    				List<ChatUsersMaster> userList = chatUsersService.getUsersMappedList(from);
    	            List<ChatUsersMaster> mappedUserList = chatUsersService.getUsersMappedList(to);
    	             
	    			List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(to);
	    			String pushMessage = null;
	    			
	    			pushMessage = "Someone has sent you a "+ callType +" call";
	    			
	    			
	    			log.info("Check deviceinfoDetails size in init call-->"+deviceinfoDetails.size());
	    			if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
	    				UserDeviceInfo userDeviceInfo = (UserDeviceInfo)deviceinfoDetails.get(0);
	            		  if(userDeviceInfo.getPlatform().equals("android") || userDeviceInfo.getPlatform().equals("ios") || userDeviceInfo.getPlatform().equals("iPhone")) {
	            			//PUSH Notification
			            	FCMPushGatewayBusinessObject fCMPushGatewayBusinessObject = new FCMPushGatewayBusinessObject();
			            	Notification notification = new Notification();
			            	
			            	PushData data1 = new PushData();
			            	data1.setSource("AET");
			            	data1.setBody(pushMessage);
			            	
			            	ChatUsersMaster fromChatuser = (ChatUsersMaster)userList.get(0);
			            	ChatUsersMaster toChatuser = (ChatUsersMaster)mappedUserList.get(0);
			            	
			            	List<Action> actionlist = new ArrayList<Action>();
			            	
			            	data1.setImage(fromChatuser.getUserphotoimageurl());
			            	data1.setName(fromChatuser.getUsername());
			            	data1.setRoomno(from);
			            	data1.setRooms(room);
			            	data1.setTargetUserId(toChatuser.getUserid());
			            	data1.setMappedUserCode(toChatuser.getUsercode());
			            	data1.setUserCode(fromChatuser.getUsercode());
			            	data1.setCallType(callType);
			            	data1.setSourceUserId(from);
			            	
			            	Action action = new Action();
			            	Action action1 = new Action();
			            	
			            	action.setId("1");
			            	action.setTitle("Accept");
			            	action.setLink("aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL");
			            	action.setRoomno(from.toString());
			            	action.setCallType(callType);
			            	action.setMappedUserCode(toChatuser.getUsercode());
			            	action.setUserCode(fromChatuser.getUsercode());
			            	action.setType("call");
			            	action.setActionColor("#33FF57");
			            	action.setRooms(room.toString());
			            	
			            	actionlist.add(action);
			            	
			            	action1.setId("2");
			            	action1.setTitle("Reject");
			            	action1.setLink(null);
			            	action1.setRoomno(null);
			            	action1.setCallType(null);
			            	action1.setMappedUserCode(null);
			            	action1.setUserCode(null);
			            	action1.setType("call");
			            	action1.setActionColor("#FF5733");
			            	
			            	actionlist.add(action1);
			            	
			            	data1.setActionlist(actionlist);
			            	
			            	String device_id = userDeviceInfo.getPushtoken();  //device id of target
			            	
			            	fCMPushGatewayBusinessObject.sendAndroidCampaignVedioPushMessage(device_id,notification,data1,userList.get(0).getUserid());
	            		 }
	    			}
    			//}
    		//}
    	//}
	}

	@PreDestroy
	public void stopSocketIOServer() {
		this.server.stop();
	}

}
