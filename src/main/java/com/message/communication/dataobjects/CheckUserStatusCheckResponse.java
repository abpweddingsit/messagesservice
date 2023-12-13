package com.message.communication.dataobjects;

public class CheckUserStatusCheckResponse {

	private Long from;
    private Long to;
    private Long room;
    private String targetuserStatus;
	public Long getFrom() {
		return from;
	}
	public void setFrom(Long from) {
		this.from = from;
	}
	public Long getTo() {
		return to;
	}
	public void setTo(Long to) {
		this.to = to;
	}
	public Long getRoom() {
		return room;
	}
	public void setRoom(Long room) {
		this.room = room;
	}
	public String getTargetuserStatus() {
		return targetuserStatus;
	}
	public void setTargetuserStatus(String targetuserStatus) {
		this.targetuserStatus = targetuserStatus;
	}
	@Override
	public String toString() {
		return "CheckUserStatusCheckResponse [from=" + from + ", to=" + to + ", room=" + room + ", targetuserStatus="
				+ targetuserStatus + "]";
	}
	
    
    

}
