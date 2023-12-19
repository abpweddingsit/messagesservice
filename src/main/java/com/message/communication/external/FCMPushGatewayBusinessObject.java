package com.message.communication.external;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.message.communication.dataobjects.Action;
import com.message.communication.dataobjects.Data;
import com.message.communication.dataobjects.FirebaseResponse;
import com.message.communication.dataobjects.HeaderRequestInterceptor;
import com.message.communication.dataobjects.Notification;
import com.message.communication.dataobjects.PushData;





public class FCMPushGatewayBusinessObject {
	private static final Logger logger = LoggerFactory.getLogger(FCMPushGatewayBusinessObject.class);
	public FCMPushGatewayBusinessObject() {
		// TODO Auto-generated constructor stub
	}
	
	final String FIREBASE_SERVER_KEY = "AAAALUVwLfE:APA91bExpRsHrbJmuzLrpHu5AT_v9m7K4CubOcZ2XaxY5HYQhz5XDnOLgnGNX_hDL6zTLphMT7naClCOce0xlKUkSu2D_UoaVqhszzzmP-XRV6zwmb0FIFOfUdbhCV8lOAq7vumGMwER";
	final String FIREBASE_CM_REST_URI = "https://fcm.googleapis.com/fcm/send";
	
	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int connectTimeout = 10000;
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
	    clientHttpRequestFactory.setConnectTimeout(connectTimeout);
	    clientHttpRequestFactory.setReadTimeout(connectTimeout);
	    return clientHttpRequestFactory;
	}
	
	public FirebaseResponse sendCampaign(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);
        
        
        FirebaseResponse firebaseResponse = restTemplate.postForObject(FIREBASE_CM_REST_URI, entity, FirebaseResponse.class);
        /*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
            logger.info(new StringBuffer("fcm.push.android: response... ").append("for MatrimoneyUserCode ..").append(matrimoneyuserCode).append("  ").append(firebaseResponse).toString());
        }*/
        
        logger.info("Sending push notifications FirebaseResponse response-->"+firebaseResponse.toString());
        
        return firebaseResponse;
    }
	
	
	// used in production
		public void sendAndroidCampaignPushMessage(final String device_id, final Notification ntObject, final Data fcmdata,Long mappeduserId) { 
	        
			logger.info(new StringBuffer("device_id is..").append(device_id).append(" for mappeduserId..").append(mappeduserId).toString());
			
			try {
				JSONArray device_ids = new JSONArray();
				device_ids.put(device_id);
				String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				JSONObject body = new JSONObject();
				body.put("registration_ids", device_ids);
				// body.put("to", "e-h-6Y9LVJs:APA91bGXP_Le7aWt7TVDVDWThAcmhUTX6fZIm2zvNwoXz3aWSt8WKT20Fq7KzG-7gjqWnRd2ymomrh90yWiyhC49-Cwdnk10r5IHTOtCFvpbx20J3Rybea6Zg8wJwoJI6Dabm9kmDzIx");
				body.put("priority", "high");
				body.put("content_available", true);
				body.put("contentAvailable", true);

				JSONObject notification = new JSONObject();
				if(ntObject!=null){
				 notification.put("body", ntObject.getBody());
				 notification.put("title", ntObject.getTitle());
				 notification.put("image", ntObject.getImage());
				}
				

				JSONObject data = new JSONObject();
				data.put("source", fcmdata.getSource());
				data.put("body", fcmdata.getBody());
				data.put("title", fcmdata.getTitle());
				data.put("clickAction", clickAction);
				data.put("image", fcmdata.getImage());
				data.put("actions", fcmdata.getActions());
				data.put("LINK", LINK);
				
				/*JSONArray action_ids = new JSONArray();
				List<Action> list = fcmdata.getActions();
				
				for(Action a : list){
					Action ac = new Action();
					
					ac.setId(a.getId());
					ac.setLink(a.getLink());
					ac.setTitle(a.getTitle());
					
					action_ids.put(ac);
				}
				data.put("actions", action_ids);*/
				

				body.put("notification", notification);
				body.put("data", data);
				
				String pushMessage = body.toString();                    // JSONUtility.toJson(pushMessageBody);
				logger.info("Sending push notifications for text chat   "+pushMessage);
				/*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
					logger.info(new StringBuffer().append("fcm.push.android: payload... ").append(pushMessage).toString());
				}*/
				
				HttpEntity<String> request = new HttpEntity<>(pushMessage);
				sendCampaign(request);
				
				logger.info("Sending push notifications to all subscribers");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		
      public void sendAndroidEngagePushMessage(final String device_id, final Notification ntObject, final PushData fcmdata,Long mappeduserId) { 
	        
			logger.info(new StringBuffer("device_id for reject is..").append(device_id).append(" for mappeduserId..").append(mappeduserId).toString());
			
			try {
				JSONArray device_ids = new JSONArray();
				device_ids.put(device_id);
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				JSONObject body = new JSONObject();
				body.put("registration_ids", device_ids);
				// body.put("to", "e-h-6Y9LVJs:APA91bGXP_Le7aWt7TVDVDWThAcmhUTX6fZIm2zvNwoXz3aWSt8WKT20Fq7KzG-7gjqWnRd2ymomrh90yWiyhC49-Cwdnk10r5IHTOtCFvpbx20J3Rybea6Zg8wJwoJI6Dabm9kmDzIx");
				body.put("priority", "high");
				body.put("content_available", true);
				body.put("contentAvailable", true);

				JSONObject notification = new JSONObject();
				if(ntObject!=null){
				 notification.put("body", ntObject.getBody());
				 notification.put("title", ntObject.getTitle());
				 notification.put("image", ntObject.getImage());
				}
				

				JSONObject data = new JSONObject();
				data.put("source", fcmdata.getSource());
				data.put("body", fcmdata.getBody());
				data.put("title", fcmdata.getTitle());
				data.put("type", "call");
				data.put("callType", "rejected");
				data.put("rooms", fcmdata.getRooms());
				data.put("targetUserId", fcmdata.getTargetUserId());
				data.put("sourceuid", fcmdata.getSourceUserId());
				data.put("calltype", fcmdata.getCallType());
				
				
				//data.put("clickAction", clickAction);
				//data.put("image", fcmdata.getImage());
				//data.put("actions", fcmdata.getActions());
				//data.put("LINK", LINK);
				
				/*JSONArray action_ids = new JSONArray();
				List<Action> list = fcmdata.getActions();
				
				for(Action a : list){
					Action ac = new Action();
					
					ac.setId(a.getId());
					ac.setLink(a.getLink());
					ac.setTitle(a.getTitle());
					
					action_ids.put(ac);
				}
				data.put("actions", action_ids);*/
				

				body.put("notification", notification);
				body.put("data", data);
				
				String pushMessage = body.toString();
				logger.info("Sending push notifications for rejection call   "+pushMessage);
				// JSONUtility.toJson(pushMessageBody);
				/*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
					logger.info(new StringBuffer().append("fcm.push.android: payload... ").append(pushMessage).toString());
				}*/
				
				HttpEntity<String> request = new HttpEntity<>(pushMessage);
				sendCampaign(request);
				
				logger.info("Sending push notifications to all subscribers for reject");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
      
      
        //For end call
        public void sendAndroidEndPushMessage(final String device_id, final Notification ntObject, final PushData fcmdata,Long mappeduserId) { 
	        
			logger.info(new StringBuffer("device_id for end is..").append(device_id).append(" for mappeduserId..").append(mappeduserId).toString());
			
			try {
				JSONArray device_ids = new JSONArray();
				device_ids.put(device_id);
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				JSONObject body = new JSONObject();
				body.put("registration_ids", device_ids);
				// body.put("to", "e-h-6Y9LVJs:APA91bGXP_Le7aWt7TVDVDWThAcmhUTX6fZIm2zvNwoXz3aWSt8WKT20Fq7KzG-7gjqWnRd2ymomrh90yWiyhC49-Cwdnk10r5IHTOtCFvpbx20J3Rybea6Zg8wJwoJI6Dabm9kmDzIx");
				body.put("priority", "high");
				body.put("content_available", true);
				body.put("contentAvailable", true);

				JSONObject notification = new JSONObject();
				if(ntObject!=null){
				 notification.put("body", fcmdata.getBody());
				 notification.put("title", fcmdata.getTitle());
				 notification.put("id", "11");
				 notification.put("type", "call");
				 notification.put("rooms", fcmdata.getRooms());
				 notification.put("targetUserId", fcmdata.getTargetUserId());
				 notification.put("callType", "endcall");
				 notification.put("sourceuid", fcmdata.getSourceUserId());
				}
				

				JSONObject data = new JSONObject();
				data.put("id", "11");
				data.put("body", fcmdata.getBody());
				data.put("title", fcmdata.getTitle());
				data.put("type", "call");
				data.put("rooms", fcmdata.getRooms());
				data.put("targetUserId", fcmdata.getTargetUserId());
				data.put("callType", "endcall");
				data.put("sourceuid", fcmdata.getSourceUserId());
				//data.put("clickAction", clickAction);
				//data.put("image", fcmdata.getImage());
				//data.put("actions", fcmdata.getActions());
				//data.put("LINK", LINK);
				
				/*JSONArray action_ids = new JSONArray();
				List<Action> list = fcmdata.getActions();
				
				for(Action a : list){
					Action ac = new Action();
					
					ac.setId(a.getId());
					ac.setLink(a.getLink());
					ac.setTitle(a.getTitle());
					
					action_ids.put(ac);
				}
				data.put("actions", action_ids);*/
				

				body.put("notification", notification);
				body.put("data", data);
				
				String pushMessage = body.toString();                    // JSONUtility.toJson(pushMessageBody);
				logger.info("Sending push notifications for end call   "+pushMessage);
				/*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
					logger.info(new StringBuffer().append("fcm.push.android: payload... ").append(pushMessage).toString());
				}*/
				
				HttpEntity<String> request = new HttpEntity<>(pushMessage);
				sendCampaign(request);
				
				logger.info("Sending push notifications to all subscribers for end ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
        
      //For end call for temporary
       public void sendAndroidEndPushMessage1(final String device_id, final Notification ntObject, final PushData fcmdata,Long mappeduserId) { 
	        
			logger.info(new StringBuffer("device_id for end checking is..").append(device_id).append(" for mappeduserId..").append(mappeduserId).toString());
			
			try {
				JSONArray device_ids = new JSONArray();
				device_ids.put(device_id);
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				JSONObject body = new JSONObject();
				body.put("registration_ids", device_ids);
				// body.put("to", "e-h-6Y9LVJs:APA91bGXP_Le7aWt7TVDVDWThAcmhUTX6fZIm2zvNwoXz3aWSt8WKT20Fq7KzG-7gjqWnRd2ymomrh90yWiyhC49-Cwdnk10r5IHTOtCFvpbx20J3Rybea6Zg8wJwoJI6Dabm9kmDzIx");
				body.put("priority", "high");
				body.put("content_available", false);
				body.put("contentAvailable", false);

				JSONObject notification = new JSONObject();
				if(ntObject!=null){
				 notification.put("body", fcmdata.getBody());
				 notification.put("title", fcmdata.getTitle());
				 notification.put("type", "call");
				 notification.put("callType", "endcall");
				}
				

				JSONObject data = new JSONObject();
				data.put("id", "11");
				data.put("body", fcmdata.getBody());
				data.put("title", fcmdata.getTitle());
				data.put("type", "call");
				data.put("callType", "endcall");
				
				//body.put("notification", notification);
				body.put("data", data);
				
				String pushMessage = body.toString();                    // JSONUtility.toJson(pushMessageBody);
				logger.info("Sending push notifications for end call 1   "+pushMessage);
				/*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
					logger.info(new StringBuffer().append("fcm.push.android: payload... ").append(pushMessage).toString());
				}*/
				
				HttpEntity<String> request = new HttpEntity<>(pushMessage);
				sendCampaign(request);
				
				logger.info("Sending push notifications to all subscribers for end ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
       
       
     //For missed call for temporary
       public void sendAndroidMissedCallPushMessage(final String device_id, final Notification ntObject, final PushData fcmdata,Long mappeduserId) { 
	        
			logger.info(new StringBuffer("device_id for Missed call is..").append(device_id).append(" for mappeduserId..").append(mappeduserId).toString());
			
			try {
				JSONArray device_ids = new JSONArray();
				device_ids.put(device_id);
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				JSONObject body = new JSONObject();
				body.put("registration_ids", device_ids);
				// body.put("to", "e-h-6Y9LVJs:APA91bGXP_Le7aWt7TVDVDWThAcmhUTX6fZIm2zvNwoXz3aWSt8WKT20Fq7KzG-7gjqWnRd2ymomrh90yWiyhC49-Cwdnk10r5IHTOtCFvpbx20J3Rybea6Zg8wJwoJI6Dabm9kmDzIx");
				body.put("priority", "high");
				body.put("content_available", true);
				body.put("contentAvailable", true);

				/*JSONObject notification = new JSONObject();
				if(ntObject!=null){
				 notification.put("body", fcmdata.getBody());
				 notification.put("title", fcmdata.getTitle());
				 notification.put("type", "call");
				 notification.put("callType", "endcall");
				}*/
				

				JSONObject data = new JSONObject();
				data.put("id", "11");
				data.put("body", fcmdata.getBody());
				data.put("title", fcmdata.getTitle());
				data.put("type", "call");
				data.put("callType", "missedcall");
				
				//body.put("notification", notification);
				body.put("data", data);
				
				String pushMessage = body.toString();                    // JSONUtility.toJson(pushMessageBody);
				logger.info("Sending push notifications for Missed call   "+pushMessage);
				/*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
					logger.info(new StringBuffer().append("fcm.push.android: payload... ").append(pushMessage).toString());
				}*/
				
				HttpEntity<String> request = new HttpEntity<>(pushMessage);
				sendCampaign(request);
				
				logger.info("Sending push notifications to all subscribers for end ");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
       
       
       public void sendAndroidMissedCallTextPushMessage(final String device_id, final Notification ntObject, final Data fcmdata,Long mappeduserId) { 
	        
			logger.info(new StringBuffer("sendAndroidMissedCallTextPushMessage device_id is..").append(device_id).append(" for mappeduserId..").append(mappeduserId).toString());
			
			try {
				JSONArray device_ids = new JSONArray();
				device_ids.put(device_id);
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
				
				JSONObject body = new JSONObject();
				body.put("registration_ids", device_ids);
				// body.put("to", "e-h-6Y9LVJs:APA91bGXP_Le7aWt7TVDVDWThAcmhUTX6fZIm2zvNwoXz3aWSt8WKT20Fq7KzG-7gjqWnRd2ymomrh90yWiyhC49-Cwdnk10r5IHTOtCFvpbx20J3Rybea6Zg8wJwoJI6Dabm9kmDzIx");
				body.put("priority", "high");
				body.put("content_available", true);
				body.put("contentAvailable", true);

				JSONObject notification = new JSONObject();
				if(ntObject!=null){
				 notification.put("body", ntObject.getBody());
				 notification.put("title", ntObject.getTitle());
				 notification.put("image", ntObject.getImage());
				}
				

				JSONObject data = new JSONObject();
				data.put("source", fcmdata.getSource());
				data.put("body", fcmdata.getBody());
				data.put("title", fcmdata.getTitle());
				//data.put("clickAction", clickAction);
				data.put("image", fcmdata.getImage());
				data.put("actions", fcmdata.getActions());
				//data.put("LINK", LINK);
				
				/*JSONArray action_ids = new JSONArray();
				List<Action> list = fcmdata.getActions();
				
				for(Action a : list){
					Action ac = new Action();
					
					ac.setId(a.getId());
					ac.setLink(a.getLink());
					ac.setTitle(a.getTitle());
					
					action_ids.put(ac);
				}
				data.put("actions", action_ids);*/
				

				body.put("notification", notification);
				body.put("data", data);
				
				String pushMessage = body.toString();                    // JSONUtility.toJson(pushMessageBody);
				logger.info("Sending push notifications for Missed call Text   "+pushMessage);
				/*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
					logger.info(new StringBuffer().append("fcm.push.android: payload... ").append(pushMessage).toString());
				}*/
				
				HttpEntity<String> request = new HttpEntity<>(pushMessage);
				sendCampaign(request);
				
				logger.info("Sending push notifications to all subscribers");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		// used in production
		public void sendAndroidCampaignVedioOnlinePushMessage(String device_id, final Notification ntObject, final PushData fcmdata,Long mappeduserId) { 
				        
						//logger.info(new StringBuffer("device_id is in sendAndroidCampaignVedioOnlinePushMessage..").append(device_id).append(" for mappeduserId in sendAndroidCampaignVedioPushMessage..").append(mappeduserId).toString());
						
						try {
							JSONArray device_ids = new JSONArray();
							//device_id = "fK0LvgOsTqKlTUgkIDji48:APA91bHKD8dKn1ERrsSfI-lNqmkKY7BXdYiFbFjRsFNdN61kanOK79pnSKWnvJei6F0BBwrcsWeWJTwvLwQKytR16ub1vbZk0E_YHFKdyJxh2SIDNo_skrDGoXaocNT6WbCrr1yMmNB-";
							device_ids.put(device_id);
							String clickAction = "aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL";
							String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST";
							//String actionlink = "aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL";
							String accepttrue = "true";
							String acceptfalse = "false";
							
							String actionLink="aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL&roomno="+fcmdata.getRoomno()+"&rooms="+fcmdata.getRooms()+"&audio=true&video=true&callaccept=Y&callinitiateByothers=remote&audioVideoType="+fcmdata.getCallType()+"&calltype="+fcmdata.getCallType()+"&userCode="+fcmdata.getUserCode()+"&mappedUserCode="+fcmdata.getMappedUserCode()+"&name="+fcmdata.getName()+"&uid="+fcmdata.getTargetUserId()+"&accept="+accepttrue+"&sourceuid="+fcmdata.getSourceUserId()+"&image="
							+fcmdata.getImage();
							
							String rejectactionLink="aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL&roomno="+fcmdata.getRoomno()+"&rooms="+fcmdata.getRooms()+"&audio=true&video=true&callaccept=Y&callinitiateByothers=remote&audioVideoType="+fcmdata.getCallType()+"&calltype="+fcmdata.getCallType()+"&userCode="+fcmdata.getUserCode()+"&mappedUserCode="+fcmdata.getMappedUserCode()+"&name="+fcmdata.getName()+"&uid="+fcmdata.getTargetUserId()+"&accept="+acceptfalse+"&sourceuid="+fcmdata.getSourceUserId()+"&image="
									+fcmdata.getImage();
							
							String callType = "";
							//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
							//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
							
							JSONObject body = new JSONObject();
							body.put("registration_ids", device_ids);
							// body.put("to", "e-h-6Y9LVJs:APA91bGXP_Le7aWt7TVDVDWThAcmhUTX6fZIm2zvNwoXz3aWSt8WKT20Fq7KzG-7gjqWnRd2ymomrh90yWiyhC49-Cwdnk10r5IHTOtCFvpbx20J3Rybea6Zg8wJwoJI6Dabm9kmDzIx");
							//body.put("mutable_content", true);
							body.put("priority", "high");
							body.put("content_available", true);
							body.put("contentAvailable", true);

							/*JSONObject notification = new JSONObject();
							if(ntObject!=null){
							 notification.put("body", "");
							 notification.put("title", "");
							 notification.put("image", "");
							 notification.put("sound", "");
							}*/
							
							if(StringUtils.isNoneEmpty(fcmdata.getCallType())) {
								if(fcmdata.getCallType().equals("voice")) {
									callType = "Voice";
								}else {
									callType = "Video";
								}
							}

							JSONObject data = new JSONObject();
							data.put("id", "11");
							data.put("title", callType+" Call from "+fcmdata.getName());
							data.put("body", "ABPWeddings");
							data.put("name", fcmdata.getName());
							data.put("image", fcmdata.getImage());
							data.put("LINK", actionLink);
							data.put("type", "call");
							data.put("rooms", fcmdata.getRooms());
							data.put("roomno", fcmdata.getRoomno());
							data.put("targetUserId", fcmdata.getTargetUserId());
							data.put("mappedUserCode", fcmdata.getMappedUserCode());
							data.put("userCode", fcmdata.getUserCode());
							data.put("callType", fcmdata.getCallType());
							data.put("action",  "ACTION_IDENTIFIER");
							data.put("clickAction", actionLink);
							data.put("sourceuid", fcmdata.getSourceUserId());
							data.put("initiatetime", System.currentTimeMillis());
							
							
							
							//JSONArray action_ids = new JSONArray();
							List<Action> action_ids = new ArrayList<Action>();
							List<Action> list = fcmdata.getActionlist();
							
							for(Action a : list){
								Action ac = new Action();
								
								ac.setId(a.getId());
								if(a.getTitle().equals("Accept")) {
									ac.setLink(actionLink);
								}else {
									ac.setLink(rejectactionLink);
								}
								
								ac.setTitle(a.getTitle());
								
								ac.setRoomno(a.getRoomno());
								ac.setRooms(a.getRooms());
								ac.setCallType(a.getCallType());
								ac.setMappedUserCode(a.getMappedUserCode());
								ac.setUserCode(a.getUserCode());
								ac.setType(a.getType());
								ac.setActionColor(a.getActionColor());
								
								action_ids.add(ac);
							}
							data.put("actions", action_ids);
							
							//logger.info("Sending push notifications actions is sendAndroidCampaignVedioOnlinePushMessage "+action_ids.toString());
							
							
							//body.put("notification", notification);
							body.put("data", data);
							
							String pushMessage = body.toString(); 
							// JSONUtility.toJson(pushMessageBody);
							/*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
								logger.info(new StringBuffer().append("fcm.push.android: payload... ").append(pushMessage).toString());
							}*/
							logger.info("Sending push notifications Online pushMessage is in  "+pushMessage);
							HttpEntity<String> request = new HttpEntity<>(pushMessage);
							sendCampaign(request);
							
							logger.info("Sending push notifications to all subscribers");
						} catch (JSONException e) {
							e.printStackTrace();
						}
		}
		
		
	// used in production
	public void sendAndroidCampaignVedioPushMessage(String device_id, final Notification ntObject, final PushData fcmdata,Long mappeduserId) { 
			        
					//logger.info(new StringBuffer("device_id is in sendAndroidCampaignVedioPushMessage..").append(device_id).append(" for mappeduserId in sendAndroidCampaignVedioPushMessage..").append(mappeduserId).toString());
					
					try {
						JSONArray device_ids = new JSONArray();
						//device_id = "fK0LvgOsTqKlTUgkIDji48:APA91bHKD8dKn1ERrsSfI-lNqmkKY7BXdYiFbFjRsFNdN61kanOK79pnSKWnvJei6F0BBwrcsWeWJTwvLwQKytR16ub1vbZk0E_YHFKdyJxh2SIDNo_skrDGoXaocNT6WbCrr1yMmNB-";
						device_ids.put(device_id);
						String clickAction = "aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL";
						String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST";
						//String actionlink = "aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL";
						String accepttrue = "true";
						String acceptfalse = "false";
						
						String actionLink="aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL&roomno="+fcmdata.getRoomno()+"&rooms="+fcmdata.getRooms()+"&audio=true&video=true&callaccept=Y&callinitiateByothers=remote&audioVideoType="+fcmdata.getCallType()+"&calltype="+fcmdata.getCallType()+"&userCode="+fcmdata.getUserCode()+"&mappedUserCode="+fcmdata.getMappedUserCode()+"&name="+fcmdata.getName()+"&uid="+fcmdata.getTargetUserId()+"&accept="+accepttrue+"&sourceuid="+fcmdata.getSourceUserId()+"&image="
						+fcmdata.getImage();
						
						String rejectactionLink="aevl://app.wed/redirect?SCREENVALUE=VIDEOCHATCALL&roomno="+fcmdata.getRoomno()+"&rooms="+fcmdata.getRooms()+"&audio=true&video=true&callaccept=Y&callinitiateByothers=remote&audioVideoType="+fcmdata.getCallType()+"&calltype="+fcmdata.getCallType()+"&userCode="+fcmdata.getUserCode()+"&mappedUserCode="+fcmdata.getMappedUserCode()+"&name="+fcmdata.getName()+"&uid="+fcmdata.getTargetUserId()+"&accept="+acceptfalse+"&sourceuid="+fcmdata.getSourceUserId()+"&image="
								+fcmdata.getImage();
						
						String callType = "";
						//String clickAction = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
						//String LINK = "aevl://app.wed/redirect?SCREENVALUE=CHATLIST&uid="+mappeduserId;
						
						JSONObject body = new JSONObject();
						body.put("registration_ids", device_ids);
						// body.put("to", "e-h-6Y9LVJs:APA91bGXP_Le7aWt7TVDVDWThAcmhUTX6fZIm2zvNwoXz3aWSt8WKT20Fq7KzG-7gjqWnRd2ymomrh90yWiyhC49-Cwdnk10r5IHTOtCFvpbx20J3Rybea6Zg8wJwoJI6Dabm9kmDzIx");
						//body.put("mutable_content", true);
						body.put("priority", "high");
						body.put("content_available", true);
						body.put("contentAvailable", true);

						JSONObject notification = new JSONObject();
						if(ntObject!=null){
						 notification.put("body", ntObject.getBody());
						 notification.put("title", ntObject.getTitle());
						 notification.put("image", ntObject.getImage());
						}
						
						if(StringUtils.isNoneEmpty(fcmdata.getCallType())) {
							if(fcmdata.getCallType().equals("voice")) {
								callType = "Voice";
							}else {
								callType = "Video";
							}
						}

						JSONObject data = new JSONObject();
						data.put("id", "11");
						data.put("title", callType+" Call from "+fcmdata.getName());
						data.put("body", "ABPWeddings");
						data.put("name", fcmdata.getName());
						data.put("image", fcmdata.getImage());
						data.put("LINK", actionLink);
						data.put("type", "call");
						data.put("rooms", fcmdata.getRooms());
						data.put("roomno", fcmdata.getRoomno());
						data.put("targetUserId", fcmdata.getTargetUserId());
						data.put("mappedUserCode", fcmdata.getMappedUserCode());
						data.put("userCode", fcmdata.getUserCode());
						data.put("callType", fcmdata.getCallType());
						data.put("action",  "ACTION_IDENTIFIER");
						data.put("clickAction", actionLink);
						data.put("sourceuid", fcmdata.getSourceUserId());
						data.put("initiatetime", System.currentTimeMillis());
						
						
						
						
						//JSONArray action_ids = new JSONArray();
						List<Action> action_ids = new ArrayList<Action>();
						List<Action> list = fcmdata.getActionlist();
						
						for(Action a : list){
							Action ac = new Action();
							
							ac.setId(a.getId());
							if(a.getTitle().equals("Accept")) {
								ac.setLink(actionLink);
							}else {
								ac.setLink(rejectactionLink);
							}
							
							ac.setTitle(a.getTitle());
							
							ac.setRoomno(a.getRoomno());
							ac.setRooms(a.getRooms());
							ac.setCallType(a.getCallType());
							ac.setMappedUserCode(a.getMappedUserCode());
							ac.setUserCode(a.getUserCode());
							ac.setType(a.getType());
							ac.setActionColor(a.getActionColor());
							
							action_ids.add(ac);
						}
						data.put("actions", action_ids);
						
						//logger.info("Sending push notifications actions is "+action_ids.toString());
						
						
						body.put("notification", notification);
						body.put("data", data);
						
						String pushMessage = body.toString(); 
						// JSONUtility.toJson(pushMessageBody);
						/*if(matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW67331594") || matrimoneyuserCode.equals("AW88079912") || matrimoneyuserCode.equals("AW45958472") || matrimoneyuserCode.equals("AW71148319") || matrimoneyuserCode.equals("AW42824190") || matrimoneyuserCode.equals("AW97176497") || matrimoneyuserCode.equals("AW88079912")){
							logger.info(new StringBuffer().append("fcm.push.android: payload... ").append(pushMessage).toString());
						}*/
						logger.info("Sending push notifications pushMessage is in sendAndroidCampaignVedioPushMessage "+pushMessage);
						HttpEntity<String> request = new HttpEntity<>(pushMessage);
						sendCampaign(request);
						
						logger.info("Sending push notifications to all subscribers");
					} catch (JSONException e) {
						e.printStackTrace();
					}
	}

}
