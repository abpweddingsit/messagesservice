package com.message.communication.dataobjects;

public class UserSpecificChatHistoryResponse {

	private Long createdon;
	private String messagebody;
	private Integer isreadmsg;
	private Long messageid;
	
	public Long getCreatedon() {
		return createdon;
	}
	public void setCreatedon(Long createdon) {
		this.createdon = createdon;
	}
	public String getMessagebody() {
		return messagebody;
	}
	public void setMessagebody(String messagebody) {
		this.messagebody = messagebody;
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
	
	
	
	
	

}
