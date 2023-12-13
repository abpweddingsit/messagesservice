package com.message.communication.dataobjects;

public class WebPushSubscription {

	private String notificationEndPoint;
	private String publicKey;
	private String auth;
	
	
	public String getNotificationEndPoint() {
		return notificationEndPoint;
	}
	public void setNotificationEndPoint(String notificationEndPoint) {
		this.notificationEndPoint = notificationEndPoint;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	
	

}
