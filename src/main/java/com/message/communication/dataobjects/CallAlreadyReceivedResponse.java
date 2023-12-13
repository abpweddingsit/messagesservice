package com.message.communication.dataobjects;

public class CallAlreadyReceivedResponse {

	private String callalreadyreceived;
	private String socketid;
	public CallAlreadyReceivedResponse() {
		// TODO Auto-generated constructor stub
	}
	public String getCallalreadyreceived() {
		return callalreadyreceived;
	}
	public void setCallalreadyreceived(String callalreadyreceived) {
		this.callalreadyreceived = callalreadyreceived;
	}
	public String getSocketid() {
		return socketid;
	}
	public void setSocketid(String socketid) {
		this.socketid = socketid;
	}
	@Override
	public String toString() {
		return "CallAlreadyReceivedResponse [callalreadyreceived=" + callalreadyreceived + ", socketid=" + socketid
				+ "]";
	}

	
}
