package com.message.communication.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.message.communication.dataobjects.Data;
import com.message.communication.dataobjects.Notification;
import com.message.communication.dataobjects.PushData;
import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserChatHistoryResponse;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.dataobjects.UserFriendListDataResponse;
import com.message.communication.dataobjects.UserFriendListMessageObject;
import com.message.communication.dataobjects.UserSpecificChatHistoryResponse;
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMapping;
import com.message.communication.entity.ChatUsersMaster;
import com.message.communication.entity.UserDeviceInfo;
import com.message.communication.external.FCMPushGatewayBusinessObject;
import com.message.communication.service.ChatUsersService;
import com.message.communication.service.S3FileStoreService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
//@RestController
@RequestMapping("/user")
public class UsersServiceRestcontroller {
	private static final Logger logger = LoggerFactory.getLogger(UsersServiceRestcontroller.class);
	private static String rootFolder = "documents/";
	
	@Autowired
	ChatUsersService chatUsersService;
	
	@Autowired
	S3FileStoreService s3FileStoreService;
	
	
	@CrossOrigin
	@PostMapping("/chat/usersCreation")
	public ResponseEntity<Map<String,Object>> usersCreation(@RequestBody Map<String, Object> req){
		
		logger.info(new StringBuffer("Insert into /chat/usersCreation..").toString());
		Long userid = 0l;
		StringBuffer usercode = new StringBuffer();
		StringBuffer username = new StringBuffer();
		StringBuffer userphotoimageurl = new StringBuffer();
		
		ChatUsersMaster chatUsersMaster = new ChatUsersMaster();
		Map<String, Object> map = new HashMap<String,Object>();
		
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
				chatUsersMaster.setUserid(userid);
			}
			if(StringUtils.isNoneEmpty((String)req.get("usercode"))) {
				usercode.append((String)req.get("usercode"));
				chatUsersMaster.setUsercode(usercode.toString());
			}
			if(StringUtils.isNoneEmpty((String)req.get("username"))) {
				username.append((String)req.get("username"));
				chatUsersMaster.setUsername(username.toString());
			}
			if(StringUtils.isNoneEmpty((String)req.get("userphotoimageurl"))) {
				userphotoimageurl.append((String)req.get("userphotoimageurl"));
				chatUsersMaster.setUserphotoimageurl(userphotoimageurl.toString());
			}
			
			chatUsersMaster.setIsactive(1);
			chatUsersMaster.setCreatedon(System.currentTimeMillis());
			
			chatUsersMaster.setVdoutboundisallowed(1);
			chatUsersMaster.setVdinboundisallowed(1);
			
			List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedList(userid);
			if(chatUser!=null && chatUser.size()>0) {
				chatUsersMaster = (ChatUsersMaster)chatUser.get(0);
				
				chatUsersMaster.setUserphotoimageurl(userphotoimageurl.toString());
				chatUsersMaster = chatUsersService.saveUsersMasters(chatUsersMaster);
				map.put("status", 1);
				map.put("msg", "User Updated successfully");
				
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}
			
			//logger.info(new StringBuffer("before calling chatUsersMaster save..").toString());
			chatUsersMaster = chatUsersService.saveUsersMasters(chatUsersMaster);
			
			//logger.info(new StringBuffer("After calling chatUsersMaster save..").append(chatUsersMaster.getUsercode()).toString());
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("status", 0);
			map.put("msg", "User does not created");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		
		map.put("status", 1);
		map.put("msg", "User created successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/usersFriendListCreation")
	public ResponseEntity<Map<String,Object>> usersFriendListCreation(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		StringBuffer usercode = new StringBuffer();
		StringBuffer username = new StringBuffer();
		StringBuffer userphotoimageurl = new StringBuffer();
		
		StringBuffer mappedusercode = new StringBuffer();
		StringBuffer mappedusername = new StringBuffer();
		StringBuffer mappeduserphotoimageurl = new StringBuffer();
		
		ChatUsersMapping chatUsersMapping = new ChatUsersMapping();
		ChatUsersMaster chatUsersMaster = new ChatUsersMaster();
		
		ChatUsersMapping mappedchatUsersMapping = new ChatUsersMapping();
		ChatUsersMaster mappedchatUsersMaster = new ChatUsersMaster();
		
		try {
			
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
				chatUsersMapping.setUserid(userid);
				
				mappedchatUsersMapping.setMappeduserid(userid);
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
				chatUsersMapping.setMappeduserid(mappeduserid);
				
				mappedchatUsersMapping.setUserid(mappeduserid);
			}
			
			//List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedList(userid);
			//List<ChatUsersMaster> mappedchatUser = chatUsersService.getUsersMappedList(mappeduserid);
			
			/*if(chatUser!=null && chatUser.size()>0) {
				chatUsersMaster = (ChatUsersMaster)chatUser.get(0);
			}*/
			
			/*if(mappedchatUser!=null && mappedchatUser.size()>0) {
				mappedchatUsersMaster = (ChatUsersMaster)chatUser.get(0);
			}*/
			
			if(StringUtils.isNoneEmpty((String)req.get("usercode"))) {
				usercode.append((String)req.get("usercode"));
				chatUsersMaster.setUsercode(usercode.toString());
				chatUsersMaster.setUserid(userid);
				chatUsersMaster.setIsactive(1);
				chatUsersMaster.setCreatedon(System.currentTimeMillis());
			}
			if(StringUtils.isNoneEmpty((String)req.get("username"))) {
				username.append((String)req.get("username"));
				chatUsersMaster.setUsername(username.toString());
			}
			if(StringUtils.isNoneEmpty((String)req.get("userphotoimageurl"))) {
				userphotoimageurl.append((String)req.get("userphotoimageurl"));
				chatUsersMaster.setUserphotoimageurl(userphotoimageurl.toString());
			}
			
			
			if(StringUtils.isNoneEmpty((String)req.get("mappedusercode"))) {
				mappedusercode.append((String)req.get("mappedusercode"));
				mappedchatUsersMaster.setUsercode(mappedusercode.toString());
				mappedchatUsersMaster.setUserid(mappeduserid);
				mappedchatUsersMaster.setIsactive(1);
				mappedchatUsersMaster.setCreatedon(System.currentTimeMillis());
			}
			if(StringUtils.isNoneEmpty((String)req.get("mappedusername"))) {
				mappedusername.append((String)req.get("mappedusername"));
				mappedchatUsersMaster.setUsername(mappedusername.toString());
			}
			if(StringUtils.isNoneEmpty((String)req.get("mappeduserphotoimageurl"))) {
				mappeduserphotoimageurl.append((String)req.get("mappeduserphotoimageurl"));
				mappedchatUsersMaster.setUserphotoimageurl(mappeduserphotoimageurl.toString());
			}
			
			
			
			/*logger.info(new StringBuffer("chatUsersMaster.getUsercode() in usersFriendListCreation..").append(chatUsersMaster.getUsercode()).toString());
			logger.info(new StringBuffer("chatUsersMaster.setUsername() in usersFriendListCreation..").append(chatUsersMaster.getUsername()).toString());
			logger.info(new StringBuffer("chatUsersMaster.setUserphotoimageurl() in usersFriendListCreation..").append(chatUsersMaster.getUserphotoimageurl()).toString());
			
			logger.info(new StringBuffer("mappedchatUsersMaster.getUsercode() in usersFriendListCreation..").append(mappedchatUsersMaster.getUsercode()).toString());
			logger.info(new StringBuffer("mappedchatUsersMaster.setUsername() in usersFriendListCreation..").append(mappedchatUsersMaster.getUsername()).toString());
			logger.info(new StringBuffer("mappedchatUsersMaster.setUserphotoimageurl() in usersFriendListCreation..").append(mappedchatUsersMaster.getUserphotoimageurl()).toString());*/
			
			
			
			
			//List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedWithProfileImageList(userid,chatUsersMaster.getUserphotoimageurl(),chatUsersMaster.getUsercode(),chatUsersMaster.getUsername());
			List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedList(userid);
			if(chatUser!=null && chatUser.size()==0) {
				chatUsersService.saveUsersMasters(chatUsersMaster);
			}else {
				ChatUsersMaster chmst = (ChatUsersMaster) chatUser.get(0);
				
				chmst.setUsercode(chatUsersMaster.getUsercode());
				chmst.setUsername(chatUsersMaster.getUsername());
				chmst.setUserphotoimageurl(chatUsersMaster.getUserphotoimageurl());
				
				chatUsersService.saveUsersMasters(chmst);
			}
			
			//List<ChatUsersMaster> mappedchatUser = chatUsersService.getUsersMappedWithProfileImageList(mappeduserid,mappedchatUsersMaster.getUserphotoimageurl(),mappedchatUsersMaster.getUsercode(),mappedchatUsersMaster.getUsername());
			List<ChatUsersMaster> mappedchatUser = chatUsersService.getUsersMappedList(mappeduserid);
			if(mappedchatUser!=null && mappedchatUser.size()==0) {
				chatUsersService.saveUsersMasters(mappedchatUsersMaster);
			}else {
				ChatUsersMaster mapchmst = (ChatUsersMaster) mappedchatUser.get(0);
				
				mapchmst.setUsercode(mappedchatUsersMaster.getUsercode());
				mapchmst.setUsername(mappedchatUsersMaster.getUsername());
				mapchmst.setUserphotoimageurl(mappedchatUsersMaster.getUserphotoimageurl());
				
				chatUsersService.saveUsersMasters(mapchmst);
			}
			
			
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			//logger.info(new StringBuffer("statusFriendList..").append(statusFriendList).toString());
			
			if(statusFriendList==false) {
				chatUsersMapping.setCreatedon(System.currentTimeMillis());
				chatUsersMapping.setUnreadcount(0);
				chatUsersMapping.setIsactive(1);
				chatUsersMapping.setModifyon(System.currentTimeMillis());
				
				chatUsersMapping.setLastmessageid(0l);
				//chatUsersMapping.setModifyon(0l);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
				
				
			}else {
				map.put("status", 1);
				map.put("msg", "Users are already mapped");
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}
			
			boolean mappedstatusFriendList = chatUsersService.getUsersMappedCheck(mappeduserid,userid);
			if(mappedstatusFriendList==false) {
				mappedchatUsersMapping.setCreatedon(System.currentTimeMillis());
				mappedchatUsersMapping.setUnreadcount(0);
				mappedchatUsersMapping.setIsactive(1);
				
				mappedchatUsersMapping.setModifyon(System.currentTimeMillis());
				
				mappedchatUsersMapping.setLastmessageid(0l);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(mappedchatUsersMapping);
			}
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("status", 0);
			map.put("msg", "Users Friend List does not created");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("status", 1);
		map.put("msg", "Users Friend List created successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/usersFriendListScheduleCreation")
	public ResponseEntity<Map<String,Object>> usersFriendListScheduleCreation(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		StringBuffer usercode = new StringBuffer();
		StringBuffer username = new StringBuffer();
		StringBuffer userphotoimageurl = new StringBuffer();
		
		StringBuffer mappedusercode = new StringBuffer();
		StringBuffer mappedusername = new StringBuffer();
		StringBuffer mappeduserphotoimageurl = new StringBuffer();
		
		ChatUsersMapping chatUsersMapping = new ChatUsersMapping();
		ChatUsersMaster chatUsersMaster = new ChatUsersMaster();
		
		ChatUsersMapping mappedchatUsersMapping = new ChatUsersMapping();
		ChatUsersMaster mappedchatUsersMaster = new ChatUsersMaster();
		
		try {
			
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
				chatUsersMapping.setUserid(userid);
				
				mappedchatUsersMapping.setMappeduserid(userid);
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
				chatUsersMapping.setMappeduserid(mappeduserid);
				
				mappedchatUsersMapping.setUserid(mappeduserid);
			}
			
			//List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedList(userid);
			//List<ChatUsersMaster> mappedchatUser = chatUsersService.getUsersMappedList(mappeduserid);
			
			/*if(chatUser!=null && chatUser.size()>0) {
				chatUsersMaster = (ChatUsersMaster)chatUser.get(0);
			}*/
			
			/*if(mappedchatUser!=null && mappedchatUser.size()>0) {
				mappedchatUsersMaster = (ChatUsersMaster)chatUser.get(0);
			}*/
			
			if(StringUtils.isNoneEmpty((String)req.get("usercode"))) {
				usercode.append((String)req.get("usercode"));
				chatUsersMaster.setUsercode(usercode.toString());
				chatUsersMaster.setUserid(userid);
				chatUsersMaster.setIsactive(1);
				chatUsersMaster.setCreatedon(System.currentTimeMillis());
			}
			if(StringUtils.isNoneEmpty((String)req.get("username"))) {
				username.append((String)req.get("username"));
				chatUsersMaster.setUsername(username.toString());
			}
			if(StringUtils.isNoneEmpty((String)req.get("userphotoimageurl"))) {
				userphotoimageurl.append((String)req.get("userphotoimageurl"));
				chatUsersMaster.setUserphotoimageurl(userphotoimageurl.toString());
			}
			
			
			if(StringUtils.isNoneEmpty((String)req.get("mappedusercode"))) {
				mappedusercode.append((String)req.get("mappedusercode"));
				mappedchatUsersMaster.setUsercode(mappedusercode.toString());
				mappedchatUsersMaster.setUserid(mappeduserid);
				mappedchatUsersMaster.setIsactive(1);
				mappedchatUsersMaster.setCreatedon(System.currentTimeMillis());
			}
			if(StringUtils.isNoneEmpty((String)req.get("mappedusername"))) {
				mappedusername.append((String)req.get("mappedusername"));
				mappedchatUsersMaster.setUsername(mappedusername.toString());
			}
			if(StringUtils.isNoneEmpty((String)req.get("mappeduserphotoimageurl"))) {
				mappeduserphotoimageurl.append((String)req.get("mappeduserphotoimageurl"));
				mappedchatUsersMaster.setUserphotoimageurl(mappeduserphotoimageurl.toString());
			}
			
			
			
			/*logger.info(new StringBuffer("chatUsersMaster.getUsercode() 1 in usersFriendListCreation..").append(chatUsersMaster.getUsercode()).toString());
			logger.info(new StringBuffer("chatUsersMaster.setUsername() 1 in usersFriendListCreation..").append(chatUsersMaster.getUsername()).toString());
			logger.info(new StringBuffer("chatUsersMaster.setUserphotoimageurl() 1 in usersFriendListCreation..").append(chatUsersMaster.getUserphotoimageurl()).toString());
			
			logger.info(new StringBuffer("mappedchatUsersMaster.getUsercode() 1 in usersFriendListCreation..").append(mappedchatUsersMaster.getUsercode()).toString());
			logger.info(new StringBuffer("mappedchatUsersMaster.setUsername() 1 in usersFriendListCreation..").append(mappedchatUsersMaster.getUsername()).toString());
			logger.info(new StringBuffer("mappedchatUsersMaster.setUserphotoimageurl() 1 in usersFriendListCreation..").append(mappedchatUsersMaster.getUserphotoimageurl()).toString());*/
			
			
			
			
			//List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedWithProfileImageList(userid,chatUsersMaster.getUserphotoimageurl(),chatUsersMaster.getUsercode(),chatUsersMaster.getUsername());
			List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedList(userid);
			if(chatUser!=null && chatUser.size()==0) {
				chatUsersService.saveUsersMasters(chatUsersMaster);
			}
			
			//List<ChatUsersMaster> mappedchatUser = chatUsersService.getUsersMappedWithProfileImageList(mappeduserid,mappedchatUsersMaster.getUserphotoimageurl(),mappedchatUsersMaster.getUsercode(),mappedchatUsersMaster.getUsername());
			List<ChatUsersMaster> mappedchatUser = chatUsersService.getUsersMappedList(mappeduserid);
			if(mappedchatUser!=null && mappedchatUser.size()==0) {
				chatUsersService.saveUsersMasters(mappedchatUsersMaster);
			}
			
			
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			logger.info(new StringBuffer("statusFriendList..").append(statusFriendList).toString());
			
			if(statusFriendList==false) {
				chatUsersMapping.setCreatedon(System.currentTimeMillis());
				chatUsersMapping.setUnreadcount(0);
				chatUsersMapping.setIsactive(1);
				chatUsersMapping.setModifyon(System.currentTimeMillis());
				
				chatUsersMapping.setLastmessageid(0l);
				//chatUsersMapping.setModifyon(0l);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
				
				
			}else {
				map.put("status", 1);
				map.put("msg", "Users are already mapped");
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}
			
			boolean mappedstatusFriendList = chatUsersService.getUsersMappedCheck(mappeduserid,userid);
			if(mappedstatusFriendList==false) {
				mappedchatUsersMapping.setCreatedon(System.currentTimeMillis());
				mappedchatUsersMapping.setUnreadcount(0);
				mappedchatUsersMapping.setIsactive(1);
				
				chatUsersMapping.setModifyon(System.currentTimeMillis());
				
				mappedchatUsersMapping.setLastmessageid(0l);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(mappedchatUsersMapping);
			}
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("status", 0);
			map.put("msg", "Users Friend List does not created");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("status", 1);
		map.put("msg", "Users Friend List created successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/usersFriendListUpdation")
	public ResponseEntity<Map<String,Object>> usersFriendListUpdation(@RequestBody Map<String, Object> req){
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		
		StringBuffer messagebody = new StringBuffer();
		Long messageid = 0l;
		
		ChatUsersMapping chatUsersMapping = new ChatUsersMapping();
		ChatMessageDetails chatMessageDetails = new ChatMessageDetails();
		
		ChatUsersMapping reverseChatUsersMapping = new ChatUsersMapping();
		
		String type = null;
		
		try {
			
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
				chatUsersMapping.setUserid(userid);
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
				chatUsersMapping.setMappeduserid(mappeduserid);
			}
			if(req.get("type")!=null) {
				type = String.valueOf(req.get("type"));
			}else {
				type = "txt";
			}
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			
			if(statusFriendList==true) {
				
				Long latestMessageId = 0l;
				
				chatMessageDetails.setUserid(userid);
				chatMessageDetails.setMappeduserid(mappeduserid);
				if(StringUtils.isNoneEmpty((String)req.get("message"))) {
					chatMessageDetails.setMessagebody((String)req.get("message"));
				}
				chatMessageDetails.setIsreadmsg(1);
				chatMessageDetails.setCreatedon(System.currentTimeMillis());
				
				if(StringUtils.isNoneEmpty((String)req.get("devPlatform"))) {
					chatMessageDetails.setDevplatform((String)req.get("devPlatform"));
				}
				chatMessageDetails.setCalltype(type);
				
				chatMessageDetails = chatUsersService.saveChatMessegeDetails(chatMessageDetails);
				
				if(chatMessageDetails!=null) {
				   List<ChatMessageDetails> mappedMessagelist = chatUsersService.getUsersMappedMessegeList(userid, mappeduserid);
				   chatMessageDetails = (ChatMessageDetails) mappedMessagelist.get(0);
				
				   latestMessageId = chatMessageDetails.getMessageid();
				}
				
				
				List<ChatUsersMapping> mappedList = chatUsersService.getUsersMappedList(userid, mappeduserid);
				List<ChatUsersMapping> reverseMappedList = chatUsersService.getUsersMappedList(mappeduserid, userid);
				if(mappedList!=null && mappedList.size()>0) {
					chatUsersMapping = (ChatUsersMapping) mappedList.get(0);
					Integer unreadCount1 = chatUsersMapping.getUnreadcount();
					
					
					chatUsersMapping.setIsactive(1);
					chatUsersMapping.setModifyon(System.currentTimeMillis());
					//chatUsersMapping.setUnreadcount(unreadCount1+1);
					chatUsersMapping.setLastmessageid(latestMessageId);
					
					chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
					
					if(reverseMappedList!=null && reverseMappedList.size()>0) {
						reverseChatUsersMapping = (ChatUsersMapping) reverseMappedList.get(0);
						Integer unreadCount = reverseChatUsersMapping.getUnreadcount();
						
						reverseChatUsersMapping.setUnreadcount(unreadCount+1);
						reverseChatUsersMapping.setModifyon(System.currentTimeMillis());
						reverseChatUsersMapping = chatUsersService.saveChatMappingDetails(reverseChatUsersMapping);
					}
				}
				
			}else {
				if(userid==mappeduserid || userid.equals(mappeduserid)) {
					logger.info(new StringBuffer("userid and mappeduserid is same value").toString());
				}else {
					chatMessageDetails.setUserid(userid);
					chatMessageDetails.setMappeduserid(mappeduserid);
					if(StringUtils.isNoneEmpty((String)req.get("message"))) {
						chatMessageDetails.setMessagebody((String)req.get("message"));
					}
					chatMessageDetails.setIsreadmsg(1);
					chatMessageDetails.setCreatedon(System.currentTimeMillis());
					
					if(StringUtils.isNoneEmpty((String)req.get("devPlatform"))) {
						chatMessageDetails.setDevplatform((String)req.get("devPlatform"));
					}
					chatMessageDetails.setCalltype(type);
					
					chatMessageDetails = chatUsersService.saveChatMessegeDetails(chatMessageDetails);
					
					if(chatMessageDetails!=null) {
						messageid = chatMessageDetails.getMessageid();
					}
					
					chatUsersMapping.setIsactive(1);
					chatUsersMapping.setCreatedon(System.currentTimeMillis());
					chatUsersMapping.setModifyon(System.currentTimeMillis());
					//chatUsersMapping.setUnreadcount(1);
					
					chatUsersMapping.setLastmessageid(messageid);
					
					chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
					
					
					reverseChatUsersMapping.setUserid(mappeduserid);
					reverseChatUsersMapping.setMappeduserid(userid);
					reverseChatUsersMapping.setUnreadcount(1);
					reverseChatUsersMapping.setModifyon(System.currentTimeMillis());
					reverseChatUsersMapping = chatUsersService.saveChatMappingDetails(reverseChatUsersMapping);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("status", 0);
			map.put("msg", "Users Friend List does not updated");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("status", 1);
		map.put("msg", "Users Friend List updated successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/usersFriendListSchedulerUpdation")
	public ResponseEntity<Map<String,Object>> usersFriendListSchedulerUpdation(@RequestBody Map<String, Object> req){
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		
		StringBuffer messagebody = new StringBuffer();
		Long messageid = 0l;
		Long createdOn = 0l;
		String message = null;
		
		ChatUsersMapping chatUsersMapping = new ChatUsersMapping();
		ChatMessageDetails chatMessageDetails = new ChatMessageDetails();
		
		ChatUsersMapping reverseChatUsersMapping = new ChatUsersMapping();
		
		try {
			
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
				chatUsersMapping.setUserid(userid);
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
				chatUsersMapping.setMappeduserid(mappeduserid);
			}
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			
			if(statusFriendList==true) {
				
				Long latestMessageId = 0l;
				
				chatMessageDetails.setUserid(userid);
				chatMessageDetails.setMappeduserid(mappeduserid);
				if(StringUtils.isNoneEmpty((String)req.get("message"))) {
					message = (String)req.get("message");
					chatMessageDetails.setMessagebody((String)req.get("message"));
				}
				chatMessageDetails.setIsreadmsg(1);
				chatMessageDetails.setCreatedon(System.currentTimeMillis());
				
				if(StringUtils.isNoneEmpty((String)req.get("devPlatform"))) {
					chatMessageDetails.setDevplatform((String)req.get("devPlatform"));
				}
				
				if(StringUtils.isNoneEmpty(String.valueOf(req.get("sentDate")))) {
					createdOn = Long.parseLong(String.valueOf(req.get("sentDate")));
					chatMessageDetails.setCreatedon(Long.parseLong(String.valueOf(req.get("sentDate"))));
				}
				
				List<ChatMessageDetails> messageDuplicateCheck = chatUsersService.getUsersMappedMessegeDuplicateCheckList(userid, mappeduserid, message, createdOn);
				
				//logger.info(new StringBuffer("userid in usersFriendListSchedulerUpdation..").append(userid).toString());
				//logger.info(new StringBuffer("mappeduserid in usersFriendListSchedulerUpdation..").append(mappeduserid).toString());
				//logger.info(new StringBuffer("message in usersFriendListSchedulerUpdation..").append(message).toString());
				//logger.info(new StringBuffer("createdOn in usersFriendListSchedulerUpdation..").append(createdOn).toString());
				
				
				//logger.info(new StringBuffer("messageDuplicateCheck size in usersFriendListSchedulerUpdation..").append(messageDuplicateCheck.size()).toString());
				
				if(messageDuplicateCheck!=null && messageDuplicateCheck.size()==0) {
				
				chatMessageDetails = chatUsersService.saveChatMessegeDetails(chatMessageDetails);
				
				if(chatMessageDetails!=null) {
				   List<ChatMessageDetails> mappedMessagelist = chatUsersService.getUsersMappedMessegeList(userid, mappeduserid);
				   chatMessageDetails = (ChatMessageDetails) mappedMessagelist.get(0);
				
				   latestMessageId = chatMessageDetails.getMessageid();
				}
				
				
				List<ChatUsersMapping> mappedList = chatUsersService.getUsersMappedList(userid, mappeduserid);
				List<ChatUsersMapping> reverseMappedList = chatUsersService.getUsersMappedList(mappeduserid, userid);
				if(mappedList!=null && mappedList.size()>0) {
					chatUsersMapping = (ChatUsersMapping) mappedList.get(0);
					Integer unreadCount1 = chatUsersMapping.getUnreadcount();
					
					
					chatUsersMapping.setIsactive(1);
					chatUsersMapping.setModifyon(createdOn);
					//chatUsersMapping.setUnreadcount(unreadCount1+1);
					chatUsersMapping.setLastmessageid(latestMessageId);
					
					chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
					
					if(reverseMappedList!=null && reverseMappedList.size()>0) {
						reverseChatUsersMapping = (ChatUsersMapping) reverseMappedList.get(0);
						Integer unreadCount = reverseChatUsersMapping.getUnreadcount();
						
						reverseChatUsersMapping.setUnreadcount(unreadCount+1);
						reverseChatUsersMapping.setModifyon(createdOn);
						reverseChatUsersMapping = chatUsersService.saveChatMappingDetails(reverseChatUsersMapping);
					}
				}
			  }
			}else {
				chatMessageDetails.setUserid(userid);
				chatMessageDetails.setMappeduserid(mappeduserid);
				if(StringUtils.isNoneEmpty((String)req.get("message"))) {
					chatMessageDetails.setMessagebody((String)req.get("message"));
				}
				if(StringUtils.isNoneEmpty(String.valueOf(req.get("sentDate")))) {
					createdOn = Long.parseLong(String.valueOf(req.get("sentDate")));
					chatMessageDetails.setCreatedon(Long.parseLong(String.valueOf(req.get("sentDate"))));
				}
				chatMessageDetails.setIsreadmsg(1);
				chatMessageDetails.setCreatedon(createdOn);
				
				if(StringUtils.isNoneEmpty((String)req.get("devPlatform"))) {
					chatMessageDetails.setDevplatform((String)req.get("devPlatform"));
				}
				
				List<ChatMessageDetails> messageDuplicateCheck = chatUsersService.getUsersMappedMessegeDuplicateCheckList(userid, mappeduserid, message, createdOn);
				
				//logger.info(new StringBuffer("userid in usersFriendListSchedulerUpdation 1..").append(userid).toString());
				//logger.info(new StringBuffer("mappeduserid in usersFriendListSchedulerUpdation 1..").append(mappeduserid).toString());
				//logger.info(new StringBuffer("message in usersFriendListSchedulerUpdation 1..").append(message).toString());
				//logger.info(new StringBuffer("createdOn in usersFriendListSchedulerUpdation 1..").append(createdOn).toString());
				
				
				if(messageDuplicateCheck!=null && messageDuplicateCheck.size()==0) {
				
					chatMessageDetails = chatUsersService.saveChatMessegeDetails(chatMessageDetails);
					
					if(chatMessageDetails!=null) {
						messageid = chatMessageDetails.getMessageid();
					}
					
					chatUsersMapping.setIsactive(1);
					chatUsersMapping.setCreatedon(System.currentTimeMillis());
					chatUsersMapping.setModifyon(createdOn);
					//chatUsersMapping.setUnreadcount(1);
					
					chatUsersMapping.setLastmessageid(messageid);
					
					chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
					
					
					reverseChatUsersMapping.setUserid(mappeduserid);
					reverseChatUsersMapping.setMappeduserid(userid);
					reverseChatUsersMapping.setUnreadcount(1);
					reverseChatUsersMapping.setModifyon(createdOn);
					reverseChatUsersMapping = chatUsersService.saveChatMappingDetails(reverseChatUsersMapping);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("status", 0);
			map.put("msg", "Users Friend List does not updated");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("status", 1);
		map.put("msg", "Users Friend List updated successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/getUsersFriendList")
	public ResponseEntity<Map<String,Object>> getUsersFriendList(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		Long userid = 0l;
		Integer vdoutboundisallowed = 0;
		
		List<UserFriendListDataObjects> list = new ArrayList<UserFriendListDataObjects>();
		
		List<UserFriendListDataResponse> responseList = new ArrayList<UserFriendListDataResponse>();
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
			}
			
			list = chatUsersService.getUserFriendList(userid);
			
			if( list.size()==0) {
				List<ChatUsersMaster> mstList = chatUsersService.getUsersMappedList(userid);
				if(mstList!=null && mstList.size()>0) {
					vdoutboundisallowed = mstList.get(0).getVdoutboundisallowed();
				}
				map.put("status", 0);
				map.put("vdoutboundisallowed", vdoutboundisallowed);
				map.put("friendlist", new ArrayList<UserFriendListDataObjects>());
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}else {
				
				for(UserFriendListDataObjects user : list) {
					UserFriendListDataResponse friendRes = new UserFriendListDataResponse();
					
					vdoutboundisallowed = user.getVdoutboundisallowed();
					
					friendRes.setMappedUserid(user.getMappeduserid());
					
					//List<UserFriendListDataObjects> friendListUpdatemsg = chatUsersService.getUsersFriendListUpdateMsg(user.getMappeduserid(), userid);
					List<UserFriendListMessageObject> userListUpdatemsg = chatUsersService.getUsersFriendListUpdateMsg(userid, user.getMappeduserid());
					
					if(userListUpdatemsg!=null && userListUpdatemsg.size()>0) {
					
					List<ChatUsersMaster> mstList = chatUsersService.getUsersMappedList(user.getMappeduserid());
					
					if(mstList!=null && mstList.size()>0) {
						
						if(StringUtils.isNotEmpty(mstList.get(0).getUsercode()) && StringUtils.isNotEmpty(mstList.get(0).getUsername()) && StringUtils.isNotEmpty(mstList.get(0).getUserphotoimageurl())) {
							//logger.info("mstList.get(0).getUsercode() 1-->"+mstList.get(0).getUsercode());
							friendRes.setMappedUserCode(mstList.get(0).getUsercode());
							friendRes.setMappedUserName(mstList.get(0).getUsername().trim());
							friendRes.setUserphotoimageurl(mstList.get(0).getUserphotoimageurl());
							
							friendRes.setCreatedon(mstList.get(0).getCreatedon());
							//friendRes.setModifyon(user.getModifyon());
							friendRes.setModifyon(userListUpdatemsg.get(0).getCreatedon());
							
							friendRes.setMessagebody(userListUpdatemsg.get(0).getMessagebody());
							friendRes.setUnreadcount(user.getUnreadcount());
							
							friendRes.setLastmessageid(user.getLastmessageid());
							
							friendRes.setIsreadmsg(userListUpdatemsg.get(0).getIsreadmsg());
							friendRes.setCalltype(userListUpdatemsg.get(0).getCalltype());
							
							friendRes.setVdinboundisallowed(mstList.get(0).getVdinboundisallowed());
														
							responseList.add(friendRes);
						}
					  }
					}else {
						List<ChatUsersMaster> mstList = chatUsersService.getUsersMappedList(user.getMappeduserid());
						if(mstList!=null && mstList.size()>0) {
							if(StringUtils.isNotEmpty(mstList.get(0).getUsercode()) && StringUtils.isNotEmpty(mstList.get(0).getUsername()) && StringUtils.isNotEmpty(mstList.get(0).getUserphotoimageurl())) {
								//logger.info("mstList.get(0).getUsercode() 2-->"+mstList.get(0).getUsercode());
								friendRes.setMappedUserCode(mstList.get(0).getUsercode());
								friendRes.setMappedUserName(mstList.get(0).getUsername().trim());
								friendRes.setUserphotoimageurl(mstList.get(0).getUserphotoimageurl());
								friendRes.setCreatedon(mstList.get(0).getCreatedon());
								friendRes.setVdinboundisallowed(mstList.get(0).getVdinboundisallowed());
								
								//friendRes.setModifyon(user.getModifyon());
								
								/*friendRes.setCreatedon(friendListUpdatemsg.get(0).getCreatedon());
								friendRes.setModifyon(friendListUpdatemsg.get(0).getModifyon());
								
								friendRes.setMessagebody(friendListUpdatemsg.get(0).getMessagebody());
								friendRes.setUnreadcount(friendListUpdatemsg.get(0).getUnreadcount());
								
								friendRes.setLastmessageid(friendListUpdatemsg.get(0).getLastmessageid());*/
								
																
								responseList.add(friendRes);
							}
						  }
					}
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("status", 0);
			map.put("friendlist", new ArrayList<UserFriendListDataObjects>());
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("status", 1);
		map.put("vdoutboundisallowed", vdoutboundisallowed);
		map.put("friendlist", responseList);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/usersSpecificChathistory")
	public ResponseEntity<Map<String,Object>> usersSoecificChathistory(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
        Long userid = 0l;
        Long mappeduserid = 0l;
        Long timestamp = 0l;
        Integer start = 0;
        Integer end = 0;
		
        List<UserChatHistoryDataObjects> list = new ArrayList<UserChatHistoryDataObjects>();
        
        
        
        
		List<UserChatHistoryDataObjects> reverselist = new ArrayList<UserChatHistoryDataObjects>();
		
		UserChatHistoryResponse chatRes = new UserChatHistoryResponse();
		
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
			}
			
			 timestamp = Long.parseLong(String.valueOf(req.get("timestamp")));
			 
			 start = Integer.parseInt(String.valueOf(req.get("start")));
			 end = Integer.parseInt(String.valueOf(req.get("end")));
			
			if(timestamp==0 && timestamp.equals(timestamp)) {
			    list = chatUsersService.getUsersChatHistoryList(mappeduserid,userid,start,end);
			    //Collections.reverse(list);
			}else {
				list = chatUsersService.getUsersChatHistoryListByTime(mappeduserid,userid,timestamp,start,end);
				//Collections.reverse(list);
			}
			
			if( list.size()==0) {
				map.put("status", 0);
				map.put("chathistory", new UserChatHistoryResponse());
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}else {
				List<UserSpecificChatHistoryResponse> chatspecificlist = new ArrayList<UserSpecificChatHistoryResponse>();
				
				for (UserChatHistoryDataObjects user : list) {
					
					
					chatRes.setMappedUserid(user.getMappeduserid());
					
					List<ChatUsersMaster> userList = chatUsersService.getUsersMappedList(user.getUserid());
					List<ChatUsersMaster> targetUserList = chatUsersService.getUsersMappedList(user.getMappeduserid());
					chatRes.setMappedUserName(targetUserList.get(0).getUsername());
					chatRes.setMappedUserCode(targetUserList.get(0).getUsercode());
					
					UserSpecificChatHistoryResponse specChatData = new UserSpecificChatHistoryResponse();
					
					
					
					specChatData.setCreatedon(user.getCreatedon());
					if(StringUtils.isNotEmpty(user.getMessagebody())) {
						if(user.getMessagebody().equals("missedCall")) 
							specChatData.setMessage("");
						else
							specChatData.setMessage(user.getMessagebody());
					}else {
						specChatData.setMessage("");
					}
					specChatData.setIsreadmsg(user.getIsreadmsg());
					specChatData.setMessageid(user.getMessageid());
					specChatData.setSenderName(userList.get(0).getUsercode());
					specChatData.setTargetUserName(targetUserList.get(0).getUsercode());
					specChatData.setType(user.getCalltype());
					
					
					chatspecificlist.add(specChatData);
					chatRes.setChatlist(chatspecificlist);
					
					
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("status", 0);
			map.put("chathistory", new UserChatHistoryResponse());
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		
		map.put("status", 1);
		map.put("chathistory", chatRes);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/removeFriendList")
	public ResponseEntity<Map<String,Object>> removeFriendList(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		
		ChatUsersMapping chatUsersMapping = new ChatUsersMapping();
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
				chatUsersMapping.setUserid(userid);
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
				chatUsersMapping.setMappeduserid(mappeduserid);
			}
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			
			if(statusFriendList == true) {
				List<ChatUsersMapping> mappedList = chatUsersService.getUsersMappedList(userid, mappeduserid);
				if(mappedList!=null && mappedList.size()>0) {
					chatUsersMapping = (ChatUsersMapping) mappedList.get(0);
					
				    chatUsersMapping.setIsactive(0);
				    chatUsersMapping.setModifyon(System.currentTimeMillis());
				    
				    chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
				}
				
				List<ChatUsersMapping> oppositemappedList = chatUsersService.getUsersMappedList(mappeduserid, userid);
				if(oppositemappedList!=null && oppositemappedList.size()>0) {
					chatUsersMapping = (ChatUsersMapping) oppositemappedList.get(0);
					
				    chatUsersMapping.setIsactive(0);
				    chatUsersMapping.setModifyon(System.currentTimeMillis());
				    
				    chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		map.put("status", 1);
		map.put("msg", "mapped users remove successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/updateunreadCount")
	public ResponseEntity<Map<String,Object>> updateunreadCount(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		
		ChatUsersMapping chatUsersMapping = new ChatUsersMapping();
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
				chatUsersMapping.setUserid(userid);
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
				chatUsersMapping.setMappeduserid(mappeduserid);
			}
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			
			if(statusFriendList == true) {
				List<ChatUsersMapping> mappedList = chatUsersService.getUsersMappedList(userid, mappeduserid);
				if(mappedList!=null && mappedList.size()>0) {
					chatUsersMapping = (ChatUsersMapping) mappedList.get(0);
					
				    chatUsersMapping.setUnreadcount(0);
				    //chatUsersMapping.setModifyon(System.currentTimeMillis());
				    
				    chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		map.put("status", 1);
		map.put("msg", "unread count remove successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@PostMapping("/chat/saveMediaFile")
	public ResponseEntity<Map<String,Object>> saveMediaFile(@RequestParam("file") MultipartFile file,@RequestParam("userid") Long userid,@RequestParam("mappeduserid") Long mappeduserid){
		Map<String, Object> map = new HashMap<String,Object>();
		
		
		
		try {
			
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			
			if(statusFriendList==true) {
				
				
				String fileNameR = "";
				String fileNameWebP = "";
				String fileName = Long.toString(new Date().getTime());
				StringBuilder folder = getFolder(userid);
				
				
				//logger.info(new StringBuffer("File fileName in saveMediaFile..").append(fileName).toString());
				//logger.info(new StringBuffer("userid in saveMediaFile..").append(userid).toString());
				//logger.info(new StringBuffer("mappeduserid in saveMediaFile..").append(mappeduserid).toString());
				
				fileNameR =s3FileStoreService.uploadS3object(file, null, folder.toString(), fileName);
				//fileNameWebP = s3FileStoreService.uploadWebpToS3(file, null, folder.toString(), fileName);
				//s3FileStoreService.resizeUploadS3object(file, null, folder.toString(), 150, fileName);
				
				String fileDownloadUriNormal = folder.insert(0, "/").append(fileNameR).toString();
				StringBuilder folder1 = getFolder(userid);
				//String fileDownloadUriWebP = folder1.insert(0, "/").append(fileNameWebP).toString();
				logger.info(new StringBuffer("fileDownloadUriNormal in saveMediaFile..").append(fileDownloadUriNormal).toString());
				//logger.info(new StringBuffer("fileDownloadUriWebP in saveMediaFile..").append(fileDownloadUriWebP).toString());
			}else {
				map.put("status", 0);
				map.put("msg", "both users are not mapped");
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@PostMapping("/chat/deviceInfoUpdate")
	public ResponseEntity<Map<String,Object>> deviceInfoUpdate(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		StringBuffer usercode = new StringBuffer();
		StringBuffer platform = new StringBuffer();
		StringBuffer appversion = new StringBuffer();
		StringBuffer buildversion = new StringBuffer();
		StringBuffer osversion = new StringBuffer();
		StringBuffer devicename = new StringBuffer();
		StringBuffer bundleid = new StringBuffer();
		StringBuffer pushtoken = new StringBuffer();
		
		UserDeviceInfo userDeviceInfo = new UserDeviceInfo();
		
		try {
			if(req.get("usercode")!=null) {
				usercode.append(req.get("usercode"));
				userDeviceInfo.setUsercode(usercode.toString());
			}
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
				userDeviceInfo.setUserid(userid);
			}
			if(req.get("platform")!=null) {
				platform.append(req.get("platform"));
				userDeviceInfo.setPlatform(platform.toString());
			}
			if(req.get("appversion")!=null) {
				appversion.append(req.get("appversion"));
				userDeviceInfo.setAppversion(appversion.toString());
			}
			if(req.get("buildversion")!=null) {
				buildversion.append(req.get("buildversion"));
				userDeviceInfo.setBuildversion(buildversion.toString());
			}
			if(req.get("osversion")!=null) {
				osversion.append(req.get("osversion"));
				userDeviceInfo.setOsversion(osversion.toString());
			}
			if(req.get("devicename")!=null) {
				devicename.append(req.get("devicename"));
				userDeviceInfo.setDevicename(devicename.toString());
			}
			if(req.get("bundleid")!=null) {
				bundleid.append(req.get("bundleid"));
				userDeviceInfo.setBundleid(bundleid.toString());
			}
			if(req.get("pushtoken")!=null) {
				pushtoken.append(req.get("pushtoken"));
				userDeviceInfo.setPushtoken(pushtoken.toString());
			}
			
			/*List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(userid);
			
			if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
				 userDeviceInfo = deviceinfoDetails.get(0);
				 
				 userDeviceInfo.setPlatform(platform.toString());
				 userDeviceInfo.setAppversion(appversion.toString());
				 userDeviceInfo.setBuildversion(buildversion.toString());
				 userDeviceInfo.setOsversion(osversion.toString());
				 userDeviceInfo.setDevicename(devicename.toString());
				 userDeviceInfo.setBundleid(bundleid.toString());
				 userDeviceInfo.setPushtoken(pushtoken.toString());
				
				 chatUsersService.saveDeviceInfo(userDeviceInfo);
			}else {*/
				
				chatUsersService.saveDeviceInfo(userDeviceInfo);
				
			//}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		map.put("status", 1);
		map.put("msg", "Device Info Update successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/statusChangeForNegativeInteraction")
	public ResponseEntity<Map<String,Object>> statusChangeForNegativeInteraction(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
			}
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			if(statusFriendList==true) {
				List<ChatUsersMapping> listMapping = chatUsersService.getUsersMappedList(userid, mappeduserid);
				
				ChatUsersMapping chatUsersMapping = (ChatUsersMapping) listMapping.get(0);
				
				chatUsersMapping.setIsactive(0);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
			}
			
			boolean statusmappedFriendList = chatUsersService.getUsersMappedCheck(mappeduserid,userid);
			if(statusmappedFriendList==true) {
				List<ChatUsersMapping> listreverseMapping = chatUsersService.getUsersMappedList(mappeduserid, userid);
				
				ChatUsersMapping chatUsersMapping = (ChatUsersMapping) listreverseMapping.get(0);
				
				chatUsersMapping.setIsactive(0);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
			}
		  	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		map.put("status", 1);
		map.put("msg", "Status Change Update successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@PostMapping("/chat/statusChangeForDeactiveInteraction")
	public ResponseEntity<Map<String,Object>> statusChangeForDeactiveInteraction(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
			}
			
			List<ChatUsersMaster> chkDuplChatMstList = chatUsersService.getUsersMappedList(userid);
			
			if(chkDuplChatMstList!=null && chkDuplChatMstList.size()>0) {
				ChatUsersMaster chatUsersMaster = (ChatUsersMaster) chkDuplChatMstList.get(0);
				
				chatUsersMaster.setIsactive(0);
				
				chatUsersService.saveUsersMasters(chatUsersMaster);
			}
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheckByuserId(userid);
			if(statusFriendList==true) {
				List<ChatUsersMapping> listMapping = chatUsersService.getUsersMappedListByUserId(userid);
				
				ChatUsersMapping chatUsersMapping = (ChatUsersMapping) listMapping.get(0);
				
				chatUsersMapping.setIsactive(0);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
			}
			
			boolean statusmappedFriendList = chatUsersService.getUsersMappedCheckBymappeduserid(userid);
			if(statusmappedFriendList==true) {
				List<ChatUsersMapping> listreverseMapping = chatUsersService.getUsersMappedListByMappeduserid(userid);
				
				ChatUsersMapping chatUsersMapping = (ChatUsersMapping) listreverseMapping.get(0);
				
				chatUsersMapping.setIsactive(0);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
			}
		  	
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		map.put("status", 1);
		map.put("msg", "Status Change Update successfully");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	public void usersFriendListMessageDeliveredUpdation(@RequestBody Map<String, Object> req){
		
		Long userid = 0l;
		Long mappeduserid = 0l;
		
		StringBuffer messagebody = new StringBuffer();
		Long messageid = 0l;
		
		
		ChatMessageDetails chatMessageDetails = new ChatMessageDetails();
		
		ChatUsersMapping reverseChatUsersMapping = new ChatUsersMapping();
		
		try {
			
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
			}
			
			boolean statusFriendList = chatUsersService.getUsersMappedCheck(userid,mappeduserid);
			
			if(statusFriendList==true) {
				
				List<ChatMessageDetails> mappedMessagelist = chatUsersService.getUsersMappedMessegeList(userid, mappeduserid);
				chatMessageDetails = (ChatMessageDetails) mappedMessagelist.get(0);
				
				//chatMessageDetails.setUserid(userid);
				//chatMessageDetails.setMappeduserid(mappeduserid);
				
				chatMessageDetails.setIsreadmsg(2);
				chatMessageDetails.setCreatedon(System.currentTimeMillis());
				
				chatMessageDetails = chatUsersService.saveChatMessegeDetails(chatMessageDetails);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@CrossOrigin
	@PostMapping("/chat/videocallrejection")
	public ResponseEntity<Map<String,Object>> videocallrejection(@RequestBody Map<String, Object> req){
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long from = 0l;
		Long to = 0l;
		Long rooms = 0l;
		
		
		String calltype = "";
		String devplatform = "";
		
		try {
			
			if(req.get("from")!=null) {
				from = Long.parseLong(String.valueOf(req.get("from")));
			}
			if(req.get("to")!=null) {
				to = Long.parseLong(String.valueOf(req.get("to")));
			}
			if(req.get("rooms")!=null) {
				rooms = Long.parseLong(String.valueOf(req.get("rooms")));
			}
			if(req.get("calltype")!=null) {
				calltype = String.valueOf(req.get("calltype"));
			}
			if(req.get("devplatform")!=null) {
				devplatform = String.valueOf(req.get("devplatform"));
			}
			
			
			log.info(" videocallrejection  from call-->"+from);
			log.info(" videocallrejection  to call-->"+to);
			log.info(" videocallrejection  rooms call-->"+rooms);
			
			
			log.info(" videocallrejection  calltype call-->"+calltype);
			log.info(" videocallrejection  devplatform call-->"+devplatform);
			
			List<UserDeviceInfo> deviceinfoDetails = chatUsersService.getUserDeviceInfoDetails(to);
			log.info("Check deviceinfoDetails size for videocallrejection call-->"+deviceinfoDetails.size());
			String pushMessage = to +" is ended the call";
			
			boolean statusFromend = chatUsersService.getUsersMappedMessegeListForMissedCall(from,to,rooms);
			boolean statusToend = chatUsersService.getUsersMappedMessegeListForMissedCall(to,from,rooms);
			
			ChatMessageDetails chatMessageDetails = new ChatMessageDetails();
			if(statusFromend == false && statusToend == false) {
				chatMessageDetails.setUserid(to);
				chatMessageDetails.setMappeduserid(from);
				chatMessageDetails.setIsreadmsg(1);
				chatMessageDetails.setCreatedon(System.currentTimeMillis());
				chatMessageDetails.setCalltype(calltype);
				chatMessageDetails.setCallduration("missedCall");
				chatMessageDetails.setMessagebody("missedCall");
				chatMessageDetails.setDevplatform(devplatform);
				chatMessageDetails.setRoomno(rooms);
			
			
			    chatUsersService.saveChatMessegeDetails(chatMessageDetails);
			}
			
			if(deviceinfoDetails!=null && deviceinfoDetails.size()>0) {
        		
        		UserDeviceInfo userDeviceInfo = (UserDeviceInfo)deviceinfoDetails.get(0);
        		  if(userDeviceInfo.getPlatform().equals("android") || userDeviceInfo.getPlatform().equals("ios") || userDeviceInfo.getPlatform().equals("iPhone")) {
        			//PUSH Notification
	            	FCMPushGatewayBusinessObject fCMPushGatewayBusinessObject = new FCMPushGatewayBusinessObject();
	            	Notification notification = new Notification();
	            	
	            	PushData data1 = new PushData();
	            	data1.setSource("AET");
	            	data1.setBody(pushMessage);
	            	
	            	data1.setRooms(rooms);
	            	data1.setSourceUserId(from);
	            	data1.setTargetUserId(to);
	            	data1.setCallType(calltype);
	            	
	            	String device_id = userDeviceInfo.getPushtoken();  //device id of target
	            	
	            	fCMPushGatewayBusinessObject.sendAndroidEngagePushMessage(device_id,notification,data1,to);
        		 }
        	}
			
		}catch(Exception e) {
		   e.printStackTrace();
		
		   map.put("status", 0);
		   map.put("msg", "call is rejected");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	   }
	
	 map.put("status", 1);
	 map.put("msg", "call is rejected successfully");
	
	return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);		
			
   }
	
	
	@CrossOrigin
	@PostMapping("/chat/vdinboundupdation")
	public ResponseEntity<Map<String,Object>> vdinboundupdation(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
		Long userid = 0l;
		
		
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
			}
			
			
			log.info(" vdinboundupdation  userid call-->"+userid);
			
			
			List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedList(userid);
			
			
			if(chatUser!=null && chatUser.size()>0) {
				ChatUsersMaster user = (ChatUsersMaster) chatUser.get(0);
				
				user.setVdinboundisallowed(1);
				
				chatUsersService.saveUsersMasters(user);
			}
			
		}catch(Exception e) {
			   e.printStackTrace();
				
			   map.put("status", 0);
			   map.put("msg", "Failed");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("status", 1);
		map.put("msg", "User Vdinboundisallowed flag is successfully updated to 1");
		
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);	
	}
	
	private StringBuilder getFolder(Long userId){
    	String userFolder = DigestUtils.md5DigestAsHex(userId.toString().getBytes()).toLowerCase();
		StringBuilder folder = new StringBuilder();
			folder.append(rootFolder);
			folder.append(userFolder);
			folder.append("/");
		return folder;
    }
}
