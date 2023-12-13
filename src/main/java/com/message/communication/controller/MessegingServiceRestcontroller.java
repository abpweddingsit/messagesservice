package com.message.communication.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.message.communication.dataobjects.PushMessageSend;
import com.message.communication.dataobjects.PushSubscription;
import com.message.communication.service.WebPushApplicationService;

@Controller
@RestController
@RequestMapping("/send")
public class MessegingServiceRestcontroller {

	private static final Logger logger = LoggerFactory.getLogger(MessegingServiceRestcontroller.class);
	
	@Autowired
	WebPushApplicationService webPushApplicationService;
	
	@CrossOrigin
	@PostMapping("/chat/subscribe")
	public ResponseEntity<Map<String,Object>> sendingWebPush(@RequestBody PushSubscription subscription){
		logger.info(new StringBuffer("Insert into /chat/webpush..").toString());
		Map<String, Object> map = new HashMap<String,Object>();
		String pushMessage = "Someone has sent you a message";
		
		try {
			//WEB Push Notification
			webPushApplicationService.sendNotifications(subscription,pushMessage);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		map.put("status", 1);
		map.put("msg", "Web Push subcribe successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/getWebPushMessage")
	public ResponseEntity<Map<String,Object>> getWebPushMessage(@RequestBody PushMessageSend pushMessageSend){
		logger.info(new StringBuffer("Insert into /chat/webpush..").toString());
		Map<String, Object> map = new HashMap<String,Object>();
		String pushMessage = "Someone has sent you a message";
		
		
		
		/*map.put("title", "Receiving chat message");
		map.put("text", pushMessage);
		map.put("image", "");
		map.put("tag", "");
		map.put("url", "");*/
		map.put("title", "Receiving chat message");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}

}
