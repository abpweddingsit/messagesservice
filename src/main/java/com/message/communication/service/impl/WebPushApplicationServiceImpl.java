package com.message.communication.service.impl;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.message.communication.dao.ChatUsersDao;
import com.message.communication.dataobjects.PushSubscription;
import com.message.communication.entity.WebPushSubscription;
import com.message.communication.service.WebPushApplicationService;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;

@Service
public class WebPushApplicationServiceImpl implements WebPushApplicationService{
	private static final Logger logger = LoggerFactory.getLogger(WebPushApplicationServiceImpl.class);
	
	@Value("${webpush.public.key}")
	private String publicKey;
	@Value("${webpush.private.key}")
	private String privateKey;
	
	@Autowired
	ChatUsersDao chatUsersDao;
	
	//private PushService pushService;
	/*private List<Subscription> subscriptions = new ArrayList<>();

	@PostConstruct
	private void init() throws GeneralSecurityException {
	  Security.addProvider(new BouncyCastleProvider());
	  pushService = new PushService(publicKey, privateKey);
	}

	private String getPublicKey() {
	  return publicKey;
	}

	private void subscribe(Subscription subscription) {
	  System.out.println("Subscribed to " + subscription.endpoint);
	  this.subscriptions.add(subscription);
	}

	private void unsubscribe(String endpoint) {
	  System.out.println("Unsubscribed from " + endpoint);
	  subscriptions = subscriptions.stream().filter(s -> !endpoint.equals(s.endpoint))
	      .collect(Collectors.toList());
	}

	private void sendNotification(Subscription subscription, String messageJson) {
	  try {
	    pushService.send(new Notification(subscription, messageJson));
	  } catch (GeneralSecurityException | IOException | JoseException | ExecutionException
	      | InterruptedException e) {
	    e.printStackTrace();
	  }
	}*/

	
	/*public void sendNotifications(String messageJson) {
	  logger.info("Sending web push notifications to all subscribers");
	
	  subscriptions.forEach(subscription -> {
	    sendNotification(subscription, String.format(messageJson, LocalTime.now()));
	  });
	}}*/
	
	
	public void sendNotifications(PushSubscription subscription,String messageJson) {
		try {
			Security.addProvider(new BouncyCastleProvider());
			PushService pushService = new PushService();
            pushService.setPublicKey(publicKey);
            pushService.setPrivateKey(privateKey);
            
            Map<String, String> data = new HashMap<String, String>();
            data.put("title", "Receiving chat message");
            data.put("text", messageJson);
            data.put("image", "");
            data.put("tag", "");
            data.put("url", "");
            JSONObject jsonObject = new JSONObject(data);
            String orgJsonData = jsonObject.toString();
            
            logger.info(new StringBuffer("orgJsonData orgJsonData is..").append(orgJsonData).toString());
            
            //Subscription pushSubscription = new Subscription(subscription.getEndpoint(),new Keys(subscription.getP256dh(),subscription.getAuth()));
            
            Subscription pushSubscription = new Subscription(subscription.getEndpoint(),subscription.getKeys());
		
            pushService.send(new Notification(pushSubscription, orgJsonData));
            
            WebPushSubscription wbpush = new WebPushSubscription();
            
            wbpush.setEndpoint(subscription.getEndpoint());
            wbpush.setP256dh(subscription.getKeys().p256dh);
            wbpush.setP256dh(subscription.getKeys().auth);
            
            
            chatUsersDao.saveWebPush(wbpush) ;
		}catch (Exception e) {
            // Handle exceptions
			e.printStackTrace();
        }
	}
	
}