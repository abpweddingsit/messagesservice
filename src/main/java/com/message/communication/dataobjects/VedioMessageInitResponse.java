package com.message.communication.dataobjects;

public class VedioMessageInitResponse {

	private Long from; 
	private Long room;
	private String calltype;
	private String fromname;
	private String userCode;
    private String mappedUserCode;
	
	public Long getFrom() {
		return from;
	}
	public void setFrom(Long from) {
		this.from = from;
	}
	public Long getRoom() {
		return room;
	}
	public void setRoom(Long room) {
		this.room = room;
	}
	public String getCalltype() {
		return calltype;
	}
	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}
	public String getFromname() {
		return fromname;
	}
	public void setFromname(String fromname) {
		this.fromname = fromname;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getMappedUserCode() {
		return mappedUserCode;
	}
	public void setMappedUserCode(String mappedUserCode) {
		this.mappedUserCode = mappedUserCode;
	}
	@Override
	public String toString() {
		return "VedioMessageInitResponse [from=" + from + ", room=" + room + ", calltype=" + calltype + ", fromname="
				+ fromname + ", userCode=" + userCode + ", mappedUserCode=" + mappedUserCode + "]";
	}
	
	
	
	

}
