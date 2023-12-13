package com.message.communication.dataobjects;

public class CalltimeResponse {
	private Long from;
    private Long to;
    private Long starttime;
    private Long endtime;
    
	public CalltimeResponse() {
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

	public Long getStarttime() {
		return starttime;
	}

	public void setStarttime(Long starttime) {
		this.starttime = starttime;
	}

	public Long getEndtime() {
		return endtime;
	}

	public void setEndtime(Long endtime) {
		this.endtime = endtime;
	}

	@Override
	public String toString() {
		return "CalltimeResponse [from=" + from + ", to=" + to + ", starttime=" + starttime + ", endtime=" + endtime
				+ "]";
	}

	
}
