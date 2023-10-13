package com.message.communication.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.message.communication.dao.ChatUsersDao;
import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMapping;
import com.message.communication.entity.ChatUsersMaster;
import com.message.communication.service.ChatUsersService;

@Service
public class ChatUsersServiceImpl implements ChatUsersService{

	@Autowired
	ChatUsersDao chatUsersDao;
	
	
	public ChatUsersMaster saveUsersMasters(ChatUsersMaster chatUsersMaster) {
		return chatUsersDao.saveUsersMasters(chatUsersMaster);
	}
	
	public boolean getUsersMappedCheck(Long userid,Long mappeduserid) {
		return chatUsersDao.getUsersMappedCheck(userid,mappeduserid);
	}
	
	public ChatMessageDetails saveChatMessegeDetails(ChatMessageDetails chatMessageDetails) {
		return chatUsersDao.saveChatMessegeDetails(chatMessageDetails);
	}

	public ChatUsersMapping saveChatMappingDetails(ChatUsersMapping chatUsersMapping) {
		return chatUsersDao.saveChatMappingDetails(chatUsersMapping);
	}
	
	public List<ChatMessageDetails> getUsersMappedMessegeList(Long userid,Long mappeduserid){
		return chatUsersDao.getUsersMappedMessegeList(userid,mappeduserid);
	}
	
	public List<ChatUsersMapping> getUsersMappedList(Long userid,Long mappeduserid){
		return chatUsersDao.getUsersMappedList(userid,mappeduserid);
	}
	
	public List<UserFriendListDataObjects> getUserFriendList(Long userid){
		return chatUsersDao.getUserFriendList(userid);
	}
	
	public List<ChatUsersMaster> getUsersMappedList(Long userid){
		return chatUsersDao.getUsersMappedList(userid);
	}
	
	public List<UserChatHistoryDataObjects> getUsersChatHistoryList(Long userid,Long mappeduserid){
		return chatUsersDao.getUsersChatHistoryList(userid,mappeduserid);
	}
}
