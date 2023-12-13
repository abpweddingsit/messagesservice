package com.message.communication.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.message.communication.dao.ChatUsersDao;
import com.message.communication.dataobjects.JabberSaveReq;
import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.dataobjects.UserFriendListMessageObject;
import com.message.communication.entity.ChatAudioVedioDetails;
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMapping;
import com.message.communication.entity.ChatUsersMaster;
import com.message.communication.entity.UserDeviceInfo;
import com.message.communication.service.ChatUsersService;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;

@Service
@EnableAsync
public class ChatUsersServiceImpl implements ChatUsersService{
	private static final Logger logger = LoggerFactory.getLogger(ChatUsersServiceImpl.class);
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
	
	public List<UserFriendListMessageObject> getUsersFriendListUpdateMsg(Long userid,Long mappeduserid){
		return chatUsersDao.getUsersFriendListUpdateMsg(userid,mappeduserid);
	}
	
	public List<ChatUsersMaster> getUsersMappedList(Long userid){
		return chatUsersDao.getUsersMappedList(userid);
	}
	
	public List<UserChatHistoryDataObjects> getUsersChatHistoryList(Long userid,Long mappeduserid,Integer start,Integer end){
		return chatUsersDao.getUsersChatHistoryList(userid,mappeduserid,start,end);
	}
	
	public List<UserChatHistoryDataObjects> getUsersChatHistoryListByTime(Long userid,Long mappeduserid,Long timestamp,Integer start,Integer end){
		return chatUsersDao.getUsersChatHistoryListByTime(userid,mappeduserid,timestamp,start,end);
	}
	
	public List<ChatUsersMaster> getUsersByUserCode(String usercode){
		return chatUsersDao.getUsersByUserCode(usercode);
	}
	
	public List<UserDeviceInfo> getUserDeviceInfoDetails(Long userid){
		return chatUsersDao.getUserDeviceInfoDetails(userid);
	}
	
	public UserDeviceInfo saveDeviceInfo(UserDeviceInfo userDeviceInfo) {
		return chatUsersDao.saveDeviceInfo(userDeviceInfo);
	}
	
	public List<ChatMessageDetails> getUsersMappedMessegeDuplicateCheckList(Long userid,Long mappeduserid,String messagebody,Long createdon){
		return chatUsersDao.getUsersMappedMessegeDuplicateCheckList(userid,mappeduserid,messagebody,createdon);
	}
	
	@Async
	public void callJabberSave( Long userid,  Long mappeduserid,String message) {
		//String url = "https://testdl.abpweddings.com/interaction-service/api/interaction/interactionqueue";
		logger.info(new StringBuffer("callJabberSave in service impl is..").append(message).toString());
		try {
			
			
		    RestTemplate rt = new RestTemplate();
		    rt.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		    rt.getMessageConverters().add(new StringHttpMessageConverter());
		    
		    int connectTimeout = 4000;
			rt.setRequestFactory(new SimpleClientHttpRequestFactory());
			SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) rt
		            .getRequestFactory();
		    rf.setConnectTimeout(connectTimeout);
		    rf.setReadTimeout(connectTimeout);
		    
		    JabberSaveReq payLoad = new JabberSaveReq();
		    payLoad.setUserid(userid);
		    payLoad.setMappeduserid(mappeduserid);
		    payLoad.setMessage(message);
		    
		    logger.info(new StringBuffer("callJabberSave in service impl payLoad is..").append(payLoad.toString()).toString());
		    
		    //set your headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<JabberSaveReq> httpEntity = new HttpEntity<JabberSaveReq>(payLoad, headers);
			
		    /*ResponseEntity<String> res = */
			//Test
			rt.exchange("https://softestweb.abpweddings.com/mats/profile/chat/jabberSave.json", HttpMethod.POST, httpEntity, String.class);
			//Prod
			//rt.exchange("https://sof.abpweddings.com/mats/profile/chat/jabberSave.json", HttpMethod.POST, httpEntity, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public boolean getUsersMappedCheckByuserId(Long userid) {
		return chatUsersDao.getUsersMappedCheckByuserId(userid);
	}
	public boolean getUsersMappedCheckBymappeduserid(Long mappeduserid) {
		return chatUsersDao.getUsersMappedCheckBymappeduserid(mappeduserid);
	}
	
	public List<ChatUsersMapping> getUsersMappedListByUserId(Long userid){
		return chatUsersDao.getUsersMappedListByUserId(userid);
	}
	public List<ChatUsersMapping> getUsersMappedListByMappeduserid(Long mappeduserid){
		return chatUsersDao.getUsersMappedListByMappeduserid(mappeduserid);
	}
	
	public List<ChatUsersMaster> getUsersMappedWithProfileImageList(Long userid,String userphotoimageurl,String usercode,String username){
		return chatUsersDao.getUsersMappedWithProfileImageList(userid,userphotoimageurl,usercode,username);
	}
	
	public ChatAudioVedioDetails saveAudiovedioMasters(ChatAudioVedioDetails chatAudioVedioDetails) {
		return chatUsersDao.saveAudiovedioMasters(chatAudioVedioDetails);
	}
	
	public List<ChatAudioVedioDetails> getAudioVedioDetailsByUserId(Long userid,Long mappeduserid){
		return chatUsersDao.getAudioVedioDetailsByUserId(userid,mappeduserid);
	}
	
	public boolean getUsersMappedMessegeListForMissedCall(Long userid,Long mappeduserid,Long room) {
		return chatUsersDao.getUsersMappedMessegeListForMissedCall(userid,mappeduserid,room);
	}
}
