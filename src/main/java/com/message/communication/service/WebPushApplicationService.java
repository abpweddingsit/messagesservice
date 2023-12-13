package com.message.communication.service;

import com.message.communication.dataobjects.PushSubscription;

public interface WebPushApplicationService {
	public void sendNotifications(PushSubscription subscription,String messageJson);
}
