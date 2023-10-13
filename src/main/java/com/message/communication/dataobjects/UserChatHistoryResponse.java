package com.message.communication.dataobjects;

import java.util.List;

public class UserChatHistoryResponse {

	private Long mappedUserid;
	private String mappedUserName;
	private String mappedUserCode;
	
	private List<UserSpecificChatHistoryResponse> chatlist;

	public Long getMappedUserid() {
		return mappedUserid;
	}

	public void setMappedUserid(Long mappedUserid) {
		this.mappedUserid = mappedUserid;
	}

	public String getMappedUserName() {
		return mappedUserName;
	}

	public void setMappedUserName(String mappedUserName) {
		this.mappedUserName = mappedUserName;
	}

	public String getMappedUserCode() {
		return mappedUserCode;
	}

	public void setMappedUserCode(String mappedUserCode) {
		this.mappedUserCode = mappedUserCode;
	}

	public List<UserSpecificChatHistoryResponse> getChatlist() {
		return chatlist;
	}

	public void setChatlist(List<UserSpecificChatHistoryResponse> chatlist) {
		this.chatlist = chatlist;
	}

	
	
	
	
	
	
	

}
