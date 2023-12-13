package com.message.communication.dataobjects;

import java.util.List;

public class PushData {
	private String source;
	
	private String title;
	private String body;
	private String image;
	//private List<Action> actions;
	private String name;
	private Long roomno;
	private Long rooms;
	private Long targetUserId;
	private String mappedUserCode;
	private String userCode;
	private String callType;
	private List<Action> actionlist;
	private Long sourceUserId;
	
	public PushData() {
		// TODO Auto-generated constructor stub
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getRoomno() {
		return roomno;
	}

	public void setRoomno(Long roomno) {
		this.roomno = roomno;
	}

	public Long getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(Long targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getMappedUserCode() {
		return mappedUserCode;
	}

	public void setMappedUserCode(String mappedUserCode) {
		this.mappedUserCode = mappedUserCode;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public List<Action> getActionlist() {
		return actionlist;
	}

	public void setActionlist(List<Action> actionlist) {
		this.actionlist = actionlist;
	}

	public Long getRooms() {
		return rooms;
	}

	public void setRooms(Long rooms) {
		this.rooms = rooms;
	}

	public Long getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(Long sourceUserId) {
		this.sourceUserId = sourceUserId;
	}
	
	

}
