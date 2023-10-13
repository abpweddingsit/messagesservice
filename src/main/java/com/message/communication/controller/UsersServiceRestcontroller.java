package com.message.communication.controller;

import java.util.ArrayList;
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

import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserChatHistoryResponse;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.dataobjects.UserFriendListDataResponse;
import com.message.communication.dataobjects.UserSpecificChatHistoryResponse;
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMapping;
import com.message.communication.entity.ChatUsersMaster;
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
			
			List<ChatUsersMaster> chatUser = chatUsersService.getUsersMappedList(userid);
			if(chatUser!=null && chatUser.size()>0) {
				chatUsersMaster = (ChatUsersMaster)chatUser.get(0);
				
				chatUsersMaster.setUserphotoimageurl(userphotoimageurl.toString());
				chatUsersMaster = chatUsersService.saveUsersMasters(chatUsersMaster);
				map.put("status", 1);
				map.put("msg", "User Updated successfully");
				
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}
			
			logger.info(new StringBuffer("before calling chatUsersMaster save..").toString());
			chatUsersMaster = chatUsersService.saveUsersMasters(chatUsersMaster);
			
			logger.info(new StringBuffer("After calling chatUsersMaster save..").append(chatUsersMaster.getUsercode()).toString());
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
			logger.info(new StringBuffer("statusFriendList..").append(statusFriendList).toString());
			
			if(statusFriendList==false) {
				chatUsersMapping.setCreatedon(System.currentTimeMillis());
				chatUsersMapping.setUnreadcount(0);
				chatUsersMapping.setIsactive(1);
				
				chatUsersMapping.setLastmessageid(0l);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
			}else {
				map.put("status", 1);
				map.put("msg", "Users are already mapped");
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
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
				if(StringUtils.isNoneEmpty((String)req.get("messagebody"))) {
					chatMessageDetails.setMessagebody((String)req.get("messagebody"));
				}
				chatMessageDetails.setIsreadmsg(1);
				chatMessageDetails.setCreatedon(System.currentTimeMillis());
				
				chatMessageDetails = chatUsersService.saveChatMessegeDetails(chatMessageDetails);
				
				if(chatMessageDetails!=null) {
				   List<ChatMessageDetails> mappedMessagelist = chatUsersService.getUsersMappedMessegeList(userid, mappeduserid);
				   chatMessageDetails = (ChatMessageDetails) mappedMessagelist.get(0);
				
				   latestMessageId = chatMessageDetails.getMessageid();
				}
				
				
				List<ChatUsersMapping> mappedList = chatUsersService.getUsersMappedList(userid, mappeduserid);
				if(mappedList!=null && mappedList.size()>0) {
					chatUsersMapping = (ChatUsersMapping) mappedList.get(0);
					
					Integer unreadCount = chatUsersMapping.getUnreadcount();
					
					chatUsersMapping.setIsactive(1);
					chatUsersMapping.setModifyon(System.currentTimeMillis());
					chatUsersMapping.setUnreadcount(unreadCount+1);
					chatUsersMapping.setLastmessageid(latestMessageId);
					
					chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
				}
				
			}else {
				chatMessageDetails.setUserid(userid);
				chatMessageDetails.setMappeduserid(mappeduserid);
				if(StringUtils.isNoneEmpty((String)req.get("messagebody"))) {
					chatMessageDetails.setMessagebody((String)req.get("messagebody"));
				}
				chatMessageDetails.setIsreadmsg(1);
				chatMessageDetails.setCreatedon(System.currentTimeMillis());
				
				chatMessageDetails = chatUsersService.saveChatMessegeDetails(chatMessageDetails);
				
				if(chatMessageDetails!=null) {
					messageid = chatMessageDetails.getMessageid();
				}
				
				chatUsersMapping.setIsactive(1);
				chatUsersMapping.setCreatedon(System.currentTimeMillis());
				chatUsersMapping.setUnreadcount(1);
				
				chatUsersMapping.setLastmessageid(messageid);
				
				chatUsersMapping = chatUsersService.saveChatMappingDetails(chatUsersMapping);
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
		
		List<UserFriendListDataObjects> list = new ArrayList<UserFriendListDataObjects>();
		
		List<UserFriendListDataResponse> responseList = new ArrayList<UserFriendListDataResponse>();
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
			}
			
			list = chatUsersService.getUserFriendList(userid);
			
			if( list.size()==0) {
				map.put("status", 0);
				map.put("friendlist", new ArrayList<UserFriendListDataObjects>());
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}else {
				
				for(UserFriendListDataObjects user : list) {
					UserFriendListDataResponse friendRes = new UserFriendListDataResponse();
					
					friendRes.setMappedUserid(user.getMappeduserid());
					
					List<ChatUsersMaster> mstList = chatUsersService.getUsersMappedList(user.getMappeduserid());
					friendRes.setMappedUserCode(mstList.get(0).getUsercode());
					friendRes.setMappedUserName(mstList.get(0).getUsername());
					friendRes.setUserphotoimageurl(mstList.get(0).getUserphotoimageurl());
					
					friendRes.setCreatedon(user.getCreatedon());
					friendRes.setModifyon(user.getModifyon());
					
					friendRes.setMessagebody(user.getMessagebody());
					friendRes.setUnreadcount(user.getUnreadcount());
					
					friendRes.setLastmessageid(user.getLastmessageid());
					
					responseList.add(friendRes);
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			
			map.put("status", 0);
			map.put("friendlist", new ArrayList<UserFriendListDataObjects>());
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		}
		
		map.put("status", 1);
		map.put("friendlist", responseList);
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping("/chat/usersSpecificChathistory")
	public ResponseEntity<Map<String,Object>> usersSoecificChathistory(@RequestBody Map<String, Object> req){
		Map<String, Object> map = new HashMap<String,Object>();
		
        Long userid = 0l;
        Long mappeduserid = 0l;
		
		List<UserChatHistoryDataObjects> list = new ArrayList<UserChatHistoryDataObjects>();
		
		UserChatHistoryResponse chatRes = new UserChatHistoryResponse();
		
		try {
			if(req.get("userid")!=null) {
				userid = Long.parseLong(String.valueOf(req.get("userid")));
			}
			if(req.get("mappeduserid")!=null) {
				mappeduserid = Long.parseLong(String.valueOf(req.get("mappeduserid")));
			}
			
			list = chatUsersService.getUsersChatHistoryList(userid,mappeduserid);
			
			if( list.size()==0) {
				map.put("status", 0);
				map.put("chathistory", new UserChatHistoryResponse());
				return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
			}else {
				List<UserSpecificChatHistoryResponse> chatspecificlist = new ArrayList<UserSpecificChatHistoryResponse>();
				
				for (UserChatHistoryDataObjects user : list) {
					
					
					chatRes.setMappedUserid(user.getMappeduserid());
					
					List<ChatUsersMaster> mstList = chatUsersService.getUsersMappedList(user.getMappeduserid());
					chatRes.setMappedUserName(mstList.get(0).getUsername());
					chatRes.setMappedUserCode(mstList.get(0).getUsercode());
					
					UserSpecificChatHistoryResponse specChatData = new UserSpecificChatHistoryResponse();
					
					specChatData.setCreatedon(user.getCreatedon());
					specChatData.setMessagebody(user.getMessagebody());
					specChatData.setIsreadmsg(user.getIsreadmsg());
					specChatData.setMessageid(user.getMessageid());
					
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
				
				
				logger.info(new StringBuffer("File fileName in saveMediaFile..").append(fileName).toString());
				logger.info(new StringBuffer("userid in saveMediaFile..").append(userid).toString());
				logger.info(new StringBuffer("mappeduserid in saveMediaFile..").append(mappeduserid).toString());
				
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
	
	private StringBuilder getFolder(Long userId){
    	String userFolder = DigestUtils.md5DigestAsHex(userId.toString().getBytes()).toLowerCase();
		StringBuilder folder = new StringBuilder();
			folder.append(rootFolder);
			folder.append(userFolder);
			folder.append("/");
		return folder;
    }
}
