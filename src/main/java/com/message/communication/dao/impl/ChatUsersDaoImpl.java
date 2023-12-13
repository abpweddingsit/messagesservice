package com.message.communication.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.message.communication.dao.ChatUsersDao;
import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.dataobjects.UserFriendListMessageObject;
import com.message.communication.entity.ChatAudioVedioDetails;
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMapping;
import com.message.communication.entity.ChatUsersMaster;
import com.message.communication.entity.UserDeviceInfo;
import com.message.communication.entity.WebPushSubscription;
import com.message.communication.repository.ChatAudioVedioDetailsRepository;
import com.message.communication.repository.ChatMessageDetailsRepository;
import com.message.communication.repository.ChatUsersMappingRepository;
import com.message.communication.repository.ChatUsersRepository;
import com.message.communication.repository.UserDeviceInfoRepository;
import com.message.communication.repository.WebPushSubscriptionRepository;

@Repository
public class ChatUsersDaoImpl implements ChatUsersDao{
	private static final Logger logger = LoggerFactory.getLogger(ChatUsersDaoImpl.class);
	
	@Autowired
	ChatUsersRepository chatUsersRepository;
	
	@Autowired
	ChatUsersMappingRepository chatUsersMappingRepository;
	
	@Autowired
	ChatMessageDetailsRepository chatMessageDetailsRepository;
	
	@Autowired
	UserDeviceInfoRepository userDeviceInfoRepository;
	
	@Autowired
	WebPushSubscriptionRepository webPushSubscriptionRepository;
	
	@Autowired
	ChatAudioVedioDetailsRepository chatAudioVedioDetailsRepository;
	
	 
	public ChatUsersMaster saveUsersMasters(ChatUsersMaster chatUsersMaster) {
		ChatUsersMaster chatUsersMst = new ChatUsersMaster();
		logger.info(new StringBuffer("chatUsersMst save..").toString());
		
		try {
			chatUsersMst = chatUsersRepository.save(chatUsersMaster);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return chatUsersMst;
		
	}
	
	public boolean getUsersMappedCheck(Long userid,Long mappeduserid) {
		boolean status = false;
		
		try {
			Integer isActive=1;
			List<ChatUsersMapping> checkMappedUsersList = chatUsersMappingRepository.findByUseridAndMappeduseridAndIsactive(userid, mappeduserid, isActive);
			logger.info(new StringBuffer("checkMappedUsersList size is ..").append(checkMappedUsersList.size()).toString());
			
			
			if(checkMappedUsersList!=null && checkMappedUsersList.size()>0) {
				status = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	public ChatMessageDetails saveChatMessegeDetails(ChatMessageDetails chatMessageDetails) {
		ChatMessageDetails chatUsersmessagesave = new ChatMessageDetails();
		
		try {
			chatUsersmessagesave = chatMessageDetailsRepository.save(chatMessageDetails);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return chatUsersmessagesave;
		
	}
	
	public ChatUsersMapping saveChatMappingDetails(ChatUsersMapping chatUsersMapping) {
		ChatUsersMapping chatUsersmappingsave = new ChatUsersMapping();
		
		try {
			chatUsersmappingsave = chatUsersMappingRepository.save(chatUsersMapping);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return chatUsersmappingsave;
		
	}
	
	public List<ChatMessageDetails> getUsersMappedMessegeList(Long userid,Long mappeduserid){
		List<ChatMessageDetails> list = new ArrayList<ChatMessageDetails>();
		
		try {
			list = chatMessageDetailsRepository.findByUseridAndMappeduseridOrderByCreatedonDesc(userid, mappeduserid);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<ChatUsersMapping> getUsersMappedList(Long userid,Long mappeduserid) {
		List<ChatUsersMapping> list = new ArrayList<ChatUsersMapping>();
		
		try {
			Integer isActive=1;
			list = chatUsersMappingRepository.findByUseridAndMappeduseridAndIsactive(userid, mappeduserid, isActive);
			logger.info(new StringBuffer("getUsersMappedList size is ..").append(list.size()).toString());
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<UserFriendListDataObjects> getUserFriendList(Long userid){
		List<UserFriendListDataObjects> list = new ArrayList<UserFriendListDataObjects>();
		
		try {
			list = chatUsersMappingRepository.getUsersFriendList(userid);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<UserFriendListMessageObject> getUsersFriendListUpdateMsg(Long userid,Long mappeduserid){
		List<UserFriendListMessageObject> list = new ArrayList<UserFriendListMessageObject>();
		
		try {
			list = chatUsersMappingRepository.getUsersFriendListUpdateMsg(userid,mappeduserid);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<UserChatHistoryDataObjects> getUsersChatHistoryList(Long userid,Long mappeduserid,Integer start,Integer end){
		List<UserChatHistoryDataObjects> list = new ArrayList<UserChatHistoryDataObjects>();
		
		try {
			list = chatUsersMappingRepository.getUsersChatHistoryList(userid,mappeduserid,start,end);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<ChatUsersMaster> getUsersMappedList(Long userid){
		List<ChatUsersMaster> list = new ArrayList<ChatUsersMaster>();
		
		try {
			list = chatUsersRepository.findByUserid(userid);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<UserChatHistoryDataObjects> getUsersChatHistoryListByTime(Long userid,Long mappeduserid,Long timestamp,Integer start,Integer end){
		List<UserChatHistoryDataObjects> list = new ArrayList<UserChatHistoryDataObjects>();
		
		try {
			list = chatUsersMappingRepository.getUsersChatHistoryListByTime(userid,mappeduserid,timestamp,start,end);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public List<ChatUsersMaster> getUsersByUserCode(String usercode){
		List<ChatUsersMaster> list = new ArrayList<ChatUsersMaster>();
		
		try {
			list = chatUsersRepository.findByUsercode(usercode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<UserDeviceInfo> getUserDeviceInfoDetails(Long userid){
		List<UserDeviceInfo> list = new ArrayList<UserDeviceInfo>();
		
		try {
			list = 	userDeviceInfoRepository.findByUseridOrderByDeviceinfoidDesc(userid);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public UserDeviceInfo saveDeviceInfo(UserDeviceInfo userDeviceInfo) {
		UserDeviceInfo uf = new UserDeviceInfo();
		try {
			uf = userDeviceInfoRepository.save(userDeviceInfo);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return uf;
	}
	
	public WebPushSubscription saveWebPush(WebPushSubscription webPushSubscription) {
		WebPushSubscription webPush = new WebPushSubscription();
		try {
			webPush = webPushSubscriptionRepository.save(webPushSubscription);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return webPush;
	}
	
	
	public List<ChatMessageDetails> getUsersMappedMessegeDuplicateCheckList(Long userid,Long mappeduserid,String messagebody,Long createdon){
		List<ChatMessageDetails> list = new ArrayList<ChatMessageDetails>();
		
		try {
			list = chatMessageDetailsRepository.findByUseridAndMappeduseridAndMessagebodyAndCreatedon(userid, mappeduserid, messagebody, createdon) ;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public boolean getUsersMappedCheckByuserId(Long userid) {
		boolean status = false;
		
		try {
			Integer isActive=1;
			List<ChatUsersMapping> checkMappedUsersList = chatUsersMappingRepository.findByUseridAndIsactive(userid,  isActive);
			logger.info(new StringBuffer("getUsersMappedCheckByuserId size is ..").append(checkMappedUsersList.size()).toString());
			
			
			if(checkMappedUsersList!=null && checkMappedUsersList.size()>0) {
				status = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	public boolean getUsersMappedCheckBymappeduserid(Long mappeduserid) {
		boolean status = false;
		
		try {
			Integer isActive=1;
			List<ChatUsersMapping> checkMappedUsersList = chatUsersMappingRepository.findByMappeduseridAndIsactive(mappeduserid,  isActive);
			logger.info(new StringBuffer("getUsersMappedCheckBymappeduserid size is ..").append(checkMappedUsersList.size()).toString());
			
			
			if(checkMappedUsersList!=null && checkMappedUsersList.size()>0) {
				status = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	public List<ChatUsersMapping> getUsersMappedListByUserId(Long userid) {
		List<ChatUsersMapping> list = new ArrayList<ChatUsersMapping>();
		
		try {
			Integer isActive=1;
			list = chatUsersMappingRepository.findByUseridAndIsactive(userid, isActive);
			logger.info(new StringBuffer("getUsersMappedListByUserId size is ..").append(list.size()).toString());
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<ChatUsersMapping> getUsersMappedListByMappeduserid(Long mappeduserid) {
		List<ChatUsersMapping> list = new ArrayList<ChatUsersMapping>();
		
		try {
			Integer isActive=1;
			list = chatUsersMappingRepository.findByMappeduseridAndIsactive( mappeduserid, isActive);
			logger.info(new StringBuffer("getUsersMappedListByMappeduserid size is ..").append(list.size()).toString());
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public List<ChatUsersMaster> getUsersMappedWithProfileImageList(Long userid,String userphotoimageurl,String usercode,String username){
		List<ChatUsersMaster> list = new ArrayList<ChatUsersMaster>();
		
		try {
			list = chatUsersRepository.findByUseridAndUserphotoimageurlAndUsercodeAndUsername(userid,userphotoimageurl.trim(),usercode.trim(),username.trim());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ChatAudioVedioDetails saveAudiovedioMasters(ChatAudioVedioDetails chatAudioVedioDetails) {
		ChatAudioVedioDetails chatAudioVedio = new ChatAudioVedioDetails();
		logger.info(new StringBuffer("chatAudioVedio save..").toString());
		
		try {
			chatAudioVedio = chatAudioVedioDetailsRepository.save(chatAudioVedioDetails);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return chatAudioVedio;
		
	}
	
	public List<ChatAudioVedioDetails> getAudioVedioDetailsByUserId(Long userid,Long mappeduserid) {
        List<ChatAudioVedioDetails> list = new ArrayList<ChatAudioVedioDetails>();
		
		try {
			list = chatAudioVedioDetailsRepository.findByUseridAndMappeduseridOrderByInittimeDesc(userid, mappeduserid) ;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	public boolean getUsersMappedMessegeListForMissedCall(Long userid,Long mappeduserid,Long room){
		List<ChatMessageDetails> list = new ArrayList<ChatMessageDetails>();
		boolean status = false;
		
		try {
			list = chatMessageDetailsRepository.findByUseridAndMappeduseridAndRoomno(userid, mappeduserid, room);
			
			if(list!=null && list.size()>0) {
				status = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return status;
	}
	
	

}
