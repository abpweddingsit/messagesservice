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
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMapping;
import com.message.communication.entity.ChatUsersMaster;
import com.message.communication.repository.ChatMessageDetailsRepository;
import com.message.communication.repository.ChatUsersMappingRepository;
import com.message.communication.repository.ChatUsersRepository;

@Repository
public class ChatUsersDaoImpl implements ChatUsersDao{
	private static final Logger logger = LoggerFactory.getLogger(ChatUsersDaoImpl.class);
	
	@Autowired
	ChatUsersRepository chatUsersRepository;
	
	@Autowired
	ChatUsersMappingRepository chatUsersMappingRepository;
	
	@Autowired
	ChatMessageDetailsRepository chatMessageDetailsRepository;
	
	
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
			
			
			if(checkMappedUsersList.size()>0) {
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
	
	public List<UserChatHistoryDataObjects> getUsersChatHistoryList(Long userid,Long mappeduserid){
		List<UserChatHistoryDataObjects> list = new ArrayList<UserChatHistoryDataObjects>();
		
		try {
			list = chatUsersMappingRepository.getUsersChatHistoryList(userid,mappeduserid);
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

}
