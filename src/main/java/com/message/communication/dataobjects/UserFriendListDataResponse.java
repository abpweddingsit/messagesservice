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
	private Integer isreadmsg;
	private String calltype;
	private Integer vdinboundisallowed;
	
	
	
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
	public Integer getIsreadmsg() {
		return isreadmsg;
	}
	public void setIsreadmsg(Integer isreadmsg) {
		this.isreadmsg = isreadmsg;
	}
	public String getCalltype() {
		return calltype;
	}
	public void setCalltype(String calltype) {
		this.calltype = calltype;
	}
	public Integer getVdinboundisallowed() {
		return vdinboundisallowed;
	}
	public void setVdinboundisallowed(Integer vdinboundisallowed) {
		this.vdinboundisallowed = vdinboundisallowed;
	}
	
	
	

}
