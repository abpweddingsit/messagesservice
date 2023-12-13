package com.message.communication.dataobjects;

public class VedioMessageNegotiationResponse {

	private Long from;
	private VedioAnswerInfo ans;
	public Long getFrom() {
		return from;
	}
	public void setFrom(Long from) {
		this.from = from;
	}
	public VedioAnswerInfo getAns() {
		return ans;
	}
	public void setAns(VedioAnswerInfo ans) {
		this.ans = ans;
	}
	@Override
	public String toString() {
		return "VedioMessageNegotiationResponse [from=" + from + ", ans=" + ans + "]";
	}
	
	

}
