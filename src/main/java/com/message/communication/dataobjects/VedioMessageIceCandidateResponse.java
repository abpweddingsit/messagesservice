package com.message.communication.dataobjects;

public class VedioMessageIceCandidateResponse {

	private Long from; 
	private VedioCandidateInfo candidate;
	public Long getFrom() {
		return from;
	}
	public void setFrom(Long from) {
		this.from = from;
	}
	public VedioCandidateInfo getCandidate() {
		return candidate;
	}
	public void setCandidate(VedioCandidateInfo candidate) {
		this.candidate = candidate;
	}
	@Override
	public String toString() {
		return "VedioMessageIceCandidateResponse [from=" + from + ", candidate=" + candidate + "]";
	}
	
	

}
