package com.message.communication.dataobjects;

public class VedioCandidateInfo {
    private String address;
    private String candidate;
    private Integer component;
    private String foundation;
    private Integer port;
    private Long priority;
    private String protocol;
    private String relatedAddress;
    private String relatedPort;
    private Integer sdpMLineIndex;
    private String sdpMid;
    private String tcpType;
    private String type;
    private String usernameFragment;
    
	public VedioCandidateInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCandidate() {
		return candidate;
	}

	public void setCandidate(String candidate) {
		this.candidate = candidate;
	}

	public Integer getComponent() {
		return component;
	}

	public void setComponent(Integer component) {
		this.component = component;
	}

	public String getFoundation() {
		return foundation;
	}

	public void setFoundation(String foundation) {
		this.foundation = foundation;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRelatedAddress() {
		return relatedAddress;
	}

	public void setRelatedAddress(String relatedAddress) {
		this.relatedAddress = relatedAddress;
	}

	public String getRelatedPort() {
		return relatedPort;
	}

	public void setRelatedPort(String relatedPort) {
		this.relatedPort = relatedPort;
	}

	public Integer getSdpMLineIndex() {
		return sdpMLineIndex;
	}

	public void setSdpMLineIndex(Integer sdpMLineIndex) {
		this.sdpMLineIndex = sdpMLineIndex;
	}

	public String getSdpMid() {
		return sdpMid;
	}

	public void setSdpMid(String sdpMid) {
		this.sdpMid = sdpMid;
	}

	public String getTcpType() {
		return tcpType;
	}

	public void setTcpType(String tcpType) {
		this.tcpType = tcpType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsernameFragment() {
		return usernameFragment;
	}

	public void setUsernameFragment(String usernameFragment) {
		this.usernameFragment = usernameFragment;
	}

	@Override
	public String toString() {
		return "VedioCandidateInfo [address=" + address + ", candidate=" + candidate + ", component=" + component
				+ ", foundation=" + foundation + ", port=" + port + ", priority=" + priority + ", protocol=" + protocol
				+ ", relatedAddress=" + relatedAddress + ", relatedPort=" + relatedPort + ", sdpMLineIndex="
				+ sdpMLineIndex + ", sdpMid=" + sdpMid + ", tcpType=" + tcpType + ", type=" + type
				+ ", usernameFragment=" + usernameFragment + "]";
	}
	
	
	

}
