package com.message.communication.dataobjects;

public class EndCallResponse {
    private Long from;
    private Long to;
	public EndCallResponse() {
		// TODO Auto-generated constructor stub
	}
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
	@Override
	public String toString() {
		return "EndCallResponse [from=" + from + ", to=" + to + "]";
	}
	
	

}
