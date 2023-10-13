package com.message.communication.dataobjects;

public class UserFriendListDataResponse {

	private Long mappedUserid;
	private String mappedUserName;
	private String mappedUserCode;
	private String userphotoimageurl;
	private Long createdon;
	private Long modifyon;
	private String messagebody;
	private Long lastmessageid;
	private Integer unreadcount;
	
	
	public Long getMappedUserid() {
		return mappedUserid;
	}
	public void setMappedUserid(Long mappedUserid) {
		this.mappedUserid = mappedUserid;
	}
	public String getMappedUserName() {
		return mappedUserName;
	}
	public void setMappedUserName(String mappedUserName) {
		this.mappedUserName = mappedUserName;
	}
	public String getMappedUserCode() {
		return mappedUserCode;
	}
	public void setMappedUserCode(String mappedUserCode) {
		this.mappedUserCode = mappedUserCode;
	}
	public Long getCreatedon() {
		return createdon;
	}
	public void setCreatedon(Long createdon) {
		this.createdon = createdon;
	}
	public Long getModifyon() {
		return modifyon;
	}
	public void setModifyon(Long modifyon) {
		this.modifyon = modifyon;
	}
	public String getMessagebody() {
		return messagebody;
	}
	public void setMessagebody(String messagebody) {
		this.messagebody = messagebody;
	}
	public Long getLastmessageid() {
		return lastmessageid;
	}
	public void setLastmessageid(Long lastmessageid) {
		this.lastmessageid = lastmessageid;
	}
	public Integer getUnreadcount() {
		return unreadcount;
	}
	public void setUnreadcount(Integer unreadcount) {
		this.unreadcount = unreadcount;
	}
	public String getUserphotoimageurl() {
		return userphotoimageurl;
	}
	public void setUserphotoimageurl(String userphotoimageurl) {
		this.userphotoimageurl = userphotoimageurl;
	}
	
	

}
