package com.message.communication.dao;

import java.util.List;

import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.dataobjects.UserFriendListMessageObject;
import com.message.communication.entity.ChatAudioVedioDetails;
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMapping;
import com.message.communication.entity.ChatUsersMaster;
import com.message.communication.entity.UserDeviceInfo;
import com.message.communication.entity.WebPushSubscription;

public interface ChatUsersDao {
	public ChatUsersMaster saveUsersMasters(ChatUsersMaster chatUsersMaster);
	public boolean getUsersMappedCheck(Long userid,Long mappeduserid);
	public ChatMessageDetails saveChatMessegeDetails(ChatMessageDetails chatMessageDetails);
	public ChatUsersMapping saveChatMappingDetails(ChatUsersMapping chatUsersMapping);
	public List<ChatMessageDetails> getUsersMappedMessegeList(Long userid,Long mappeduserid);
	public List<ChatUsersMapping> getUsersMappedList(Long userid,Long mappeduserid);
	public List<UserFriendListDataObjects> getUserFriendList(Long userid);
	public List<UserFriendListMessageObject> getUsersFriendListUpdateMsg(Long userid,Long mappeduserid);
	public List<UserChatHistoryDataObjects> getUsersChatHistoryList(Long userid,Long mappeduserid,Integer start,Integer end);
	public List<ChatUsersMaster> getUsersMappedList(Long userid);
	public List<UserChatHistoryDataObjects> getUsersChatHistoryListByTime(Long userid,Long mappeduserid,Long timestamp,Integer start,Integer end);
	
	public List<ChatUsersMaster> getUsersByUserCode(String usercode);
	public List<UserDeviceInfo> getUserDeviceInfoDetails(Long userid);
	public UserDeviceInfo saveDeviceInfo(UserDeviceInfo userDeviceInfo);
	
	public WebPushSubscription saveWebPush(WebPushSubscription webPushSubscription);
	
	public List<ChatMessageDetails> getUsersMappedMessegeDuplicateCheckList(Long userid,Long mappeduserid,String messagebody,Long createdon);
	
	public boolean getUsersMappedCheckByuserId(Long userid);
	public boolean getUsersMappedCheckBymappeduserid(Long mappeduserid);
	
	public List<ChatUsersMapping> getUsersMappedListByUserId(Long userid);
	public List<ChatUsersMapping> getUsersMappedListByMappeduserid(Long mappeduserid);
	
	public List<ChatUsersMaster> getUsersMappedWithProfileImageList(Long userid,String userphotoimageurl,String usercode,String username);
	
	public ChatAudioVedioDetails saveAudiovedioMasters(ChatAudioVedioDetails chatAudioVedioDetails);
	public List<ChatAudioVedioDetails> getAudioVedioDetailsByUserId(Long userid,Long mappeduserid);
	
	public boolean getUsersMappedMessegeListForMissedCall(Long userid,Long mappeduserid,Long room);
	
}
