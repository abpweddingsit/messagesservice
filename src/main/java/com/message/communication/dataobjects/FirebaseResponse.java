package com.message.communication.dataobjects;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FirebaseResponse {
	
	private long multicast_id;
    private Integer success;
    private Integer failure;
    private Object canonical_ids;
    private Object results;

	public FirebaseResponse() {
		// TODO Auto-generated constructor stub
	}

	public long getMulticast_id() {
		return multicast_id;
	}

	public void setMulticast_id(long multicast_id) {
		this.multicast_id = multicast_id;
	}

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public Integer getFailure() {
		return failure;
	}

	public void setFailure(Integer failure) {
		this.failure = failure;
	}

	public Object getCanonical_ids() {
		return canonical_ids;
	}

	public void setCanonical_ids(Object canonical_ids) {
		this.canonical_ids = canonical_ids;
	}

	public Object getResults() {
		return results;
	}

	public void setResults(Object results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
