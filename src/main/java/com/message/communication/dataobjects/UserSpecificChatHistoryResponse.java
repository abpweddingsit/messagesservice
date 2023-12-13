package com.message.communication.dataobjects;

public class UserSpecificChatHistoryResponse {

	private Long createdon;
	private String message;
	private Integer isreadmsg;
	private Long messageid;
	private String senderName;
	private String targetUserName;
	private String type;
	
	
	public Long getCreatedon() {
		return createdon;
	}
	public void setCreatedon(Long createdon) {
		this.createdon = createdon;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getIsreadmsg() {
		return isreadmsg;
	}
	public void setIsreadmsg(Integer isreadmsg) {
		this.isreadmsg = isreadmsg;
	}
	public Long getMessageid() {
		return messageid;
	}
	public void setMessageid(Long messageid) {
		this.messageid = messageid;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getTargetUserName() {
		return targetUserName;
	}
	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	
	

}
