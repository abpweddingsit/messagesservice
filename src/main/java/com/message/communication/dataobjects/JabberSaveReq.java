package com.message.communication.dataobjects;

public class JabberSaveReq {

	private Long userid;
	private Long mappeduserid;
	private String message;
	public JabberSaveReq() {
		// TODO Auto-generated constructor stub
	}
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public Long getMappeduserid() {
		return mappeduserid;
	}
	public void setMappeduserid(Long mappeduserid) {
		this.mappeduserid = mappeduserid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	

}
