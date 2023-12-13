package com.message.communication.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.message.communication.dataobjects.Data;
import com.message.communication.dataobjects.Message;
import com.message.communication.dataobjects.Notification;
import com.message.communication.dataobjects.VedioMessage;
import com.message.communication.entity.ChatUsersMaster;
import com.message.communication.entity.UserDeviceInfo;
import com.message.communication.external.FCMPushGatewayBusinessObject;
import com.message.communication.service.ChatUsersService;
import com.message.communication.service.WebPushApplicationService;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class SocketIOController {
	private static final Logger logger = LoggerFactory.getLogger(SocketIOController.class);
	static private Socket socket;
	
	@Autowired
    private SocketIOServer socketServer;
	
	@Autowired
	ChatUsersService chatUsersService;
	
	@Autowired
	UsersServiceRestcontroller usersServiceRestcontroller;
	
	@Autowired
	WebPushApplicationService webPushApplicationService;
	
	ConcurrentMap<String, String> connectedClients = new ConcurrentHashMap<>();
	ConcurrentMap<String, Map<String,String>> callRoomMap = new ConcurrentHashMap<>();
	ConcurrentMap<String, String> socArrMap = new ConcurrentHashMap<>();
	
	
	SocketIOController(SocketIOServer socketServer){
        this.socketServer=socketServer;

        this.socketServer.addConnectListener(onUserConnectWithSocket);
        this.socketServer.addDisconnectListener(onUserDisconnectWithSocket);

        /**
         * Here we create only one event listener
         * but we can create any number of listener
         * messageSendToUser is socket end point after socket connection user have to send message payload on messageSendToUser event
         */
        //this.socketServer.addEventListener("messageSendToUser", Message.class, onSendMessage);

    }
	
	public ConnectListener onUserConnectWithSocket = new ConnectListener() {
        @Override
        public void onConnect(SocketIOClient client) {
        	//logger.info("Perform operation on user connect in controller");
        	
        	
        	
        }
    };


    public DisconnectListener onUserDisconnectWithSocket = new DisconnectListener() {
        @Override
        public void onDisconnect(SocketIOClient client) {
        	//logger.info("Perform operation on user disconnect in controller");
        	ConcurrentMap<String, String> connectedClients = new ConcurrentHashMap<>();
        }
    };
    
    public DataListener<Message> onSendMessage = new DataListener<Message>() {
        @Override
        public void onData(SocketIOClient client, Message message, AckRequest acknowledge) throws Exception {

         /**
             * Sending message to target user
             * target user should subscribe the socket event with his/her name.
             * Send the same payload to user
             */
        	//message.setCreatedon(Long.toString(System.currentTimeMillis()));
        	logger.info(message.getSenderName()+" user send message to user "+message.getTargetUserName()+" and message is "+message.getMessage()+" and createdon is "+message.getCreatedon()+" and type is "+message.getType()+" and modifyon is "+message.getModifyon()+ " and devPlatform "+message.getDevPlatform()+ " and type "+message.getType());
        	
        	 List<ChatUsersMaster> userList = chatUsersService.getUsersByUserCode(message.getSenderName());
             List<ChatUsersMaster> mappedUserList = chatUsersService.getUsersByUserCode(message.getTargetUserName());
             
        	socketServer.addConnectListener(new ConnectListener() {
            	@Override
                public void onConnect(SocketIOClient client) {
                  //logger.info("client connected! "+message.getTargetUserName());
                  String targetusername=message.getTargetUserName();
                  //client.set("targetusername",targetusername);
                  if(StringUtils.isNotEmpty(targetusername)) {
                    connectedClients.put(targetusername, targetusername);
                  }
                  //userIdToSocketIdMap.put(mappedUserList.get(0).getUserid(), mappedUserList.get(0).getUserid());
                }
            });
        	
        	try {
        	socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(),client, message);
        	logger.info("socketServer.getBroadcastOperations().sendEvent()....");
        	}catch (Exception e) {
                // Handle exceptions
                e.printStackTrace();
            }
            //System.out.println(message.getSenderName()+" user send message to user "+message.getTargetUserName()+" and message is "+message.getMessage());
        	
            Map<String, Object> map = new HashMap<String, Object>();
            
           
            
            map.put("userid", userList.get(0).getUserid());
            map.put("mappeduserid", mappedUserList.get(0).getUserid());
            map.put("message", message.getMessage());
            map.put("type", message.getType());
            map.put("devPlatform", message.getDevPlatform());
            
            usersServiceRestcontroller.usersFriendListUpdation(map);
            
            chatUsersService.callJabberSave(userList.get(0).getUserid(), mappedUserList.get(0).getUserid(), message.getMessage());
			
			//Prod
			//String toJID = mappedScreenVal+"@ip-10-200-18-60.ap-southeast-1.compute.internal";
            
            //socketServer.getBroadcastOperations().sendEvent(message.getTargetUserName(),client, message);
            
            /**
             * After sending message to target user we can send acknowledge to sender
             */
            acknowledge.sendAckData("Message send to target user successfully");
            
            
            
            logger.info("Check a user is online or not-->"+message.getTargetUserName()+" "+isUserOnline(message.getTargetUserName()));
            
            if(isUserOnline(message.getTargetUserName())==false) {
            	List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(mappedUserList.get(0).getUserid());
            	
            	logger.info("Check deviceinfoDetails size-->"+deviceinfoDetails.size());
            	
            	String pushMessage = "Someone has sent you a message";
            	
            	/*if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
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
		            	
		            	fCMPushGatewayBusinessObject.sendAndroidCampaignPushMessage(device_id,notification,data,userList.get(0).getUserid());
            		 }
            	}*/
            	
            	
            	
            }
       
        	
        	
            socketServer.addDisconnectListener(new DisconnectListener() {
				
				@Override
				public void onDisconnect(SocketIOClient client) {
					// TODO Auto-generated method stub
					//logger.info("client disconnected !"+message.getTargetUserName());
					if(StringUtils.isNotEmpty(message.getTargetUserName())) {
					 String remove_value = (String)connectedClients.remove(message.getTargetUserName());
					 //userIdToSocketIdMap.remove(mappedUserList.get(0).getUserid());
	            	 //logger.info("HashMap is remove value is-->"+remove_value);	
					}
				}
			});
            /*else {
            	 Map<String, Object> mapdelivered = new HashMap<String, Object>();
            	
            	 mapdelivered.put("userid", userList.get(0).getUserid());
            	 mapdelivered.put("mappeduserid", mappedUserList.get(0).getUserid());
                 
                 usersServiceRestcontroller.usersFriendListMessageDeliveredUpdation(mapdelivered);
            }*/
            //connectedClients.remove("targetusername",message.getTargetUserName());
            
            
           // socketServer.addConnectListener(connectedClients.put(message.getTargetUserName(), client));
            /*try {
                client(message);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }*/
            
            
        }
        
               
        
        
    };
    
    
    public boolean isUserOnline(String userId) {
    	if(StringUtils.isNotEmpty(userId)) {
            return connectedClients.containsKey(userId);
    	}else {
    		return false;
    	}
    }
    
    
    
    public static void client(Message message) throws URISyntaxException, InterruptedException {
    	logger.info("client........");
        socket = IO.socket("http://localhost:8878" );
        /*socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                socket.emit("toServer", "connected");
                socket.send("test");
            }
        });*/
        socket.on(message.getTargetUserName(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            	logger.info("Client recievd : " + args[0]);

            }
        });
        /*socket.connect();
        while (!socket.connected())
            Thread.sleep(50);
        socket.send("another test");
        Thread.sleep(10000);
        socket.disconnect();*/
    }

}
