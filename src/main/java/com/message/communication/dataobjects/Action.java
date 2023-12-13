package com.message.communication.dataobjects;

public class Action {
	private String id;
	private String title;
	private String link;
	private String roomno;
	private String rooms;
	private String callType;
	private String mappedUserCode;
	private String userCode;
	private String type;
	private String actionColor;
	
	public Action() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getRoomno() {
		return roomno;
	}

	public void setRoomno(String roomno) {
		this.roomno = roomno;
	}

	public String getRooms() {
		return rooms;
	}

	public void setRooms(String rooms) {
		this.rooms = rooms;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActionColor() {
		return actionColor;
	}

	public void setActionColor(String actionColor) {
		this.actionColor = actionColor;
	}

	@Override
	public String toString() {
		return "Action [id=" + id + ", title=" + title + ", link=" + link + ", roomno=" + roomno + ", rooms=" + rooms
				+ ", callType=" + callType + ", mappedUserCode=" + mappedUserCode + ", userCode=" + userCode + ", type="
				+ type + ", actionColor=" + actionColor + "]";
	}
	
	

}
