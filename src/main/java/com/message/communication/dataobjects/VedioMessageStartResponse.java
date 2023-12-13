package com.message.communication.dataobjects;

public class VedioMessageStartResponse {

	private Long from; 
	private VedioOfferInfo offer;
	public Long getFrom() {
		return from;
	}
	public void setFrom(Long from) {
		this.from = from;
	}
	public VedioOfferInfo getOffer() {
		return offer;
	}
	public void setOffer(VedioOfferInfo offer) {
		this.offer = offer;
	}
	@Override
	public String toString() {
		return "VedioMessageStartResponse [from=" + from + ", offer=" + offer + "]";
	}
	
	
	
}
