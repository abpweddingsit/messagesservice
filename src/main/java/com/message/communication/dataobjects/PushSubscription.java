package com.message.communication.dataobjects;

import nl.martijndwars.webpush.Subscription.Keys;

public class PushSubscription {

	private String endpoint;
	private String expirationTime;
	//private String p256dh;
    //private String auth;
	private Keys keys;
    
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	/*public String getP256dh() {
		return p256dh;
	}
	public void setP256dh(String p256dh) {
		this.p256dh = p256dh;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}*/
	
	public Keys getKeys() {
		return keys;
	}
	public String getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}
	public void setKeys(Keys keys) {
		this.keys = keys;
	}
	
	
	
	
	

}
