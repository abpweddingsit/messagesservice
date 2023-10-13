package com.message.communication.service;

import java.util.List;

import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.entity.ChatMessageDetails;
import com.message.communication.entity.ChatUsersMapping;
import com.message.communication.entity.ChatUsersMaster;

public interface ChatUsersService {
   public ChatUsersMaster saveUsersMasters(ChatUsersMaster chatUsersMaster);
   public boolean getUsersMappedCheck(Long userid,Long mappeduserid);
   public ChatMessageDetails saveChatMessegeDetails(ChatMessageDetails chatMessageDetails);
   public ChatUsersMapping saveChatMappingDetails(ChatUsersMapping chatUsersMapping);
   public List<ChatMessageDetails> getUsersMappedMessegeList(Long userid,Long mappeduserid);
   public List<ChatUsersMapping> getUsersMappedList(Long userid,Long mappeduserid);
   public List<UserFriendListDataObjects> getUserFriendList(Long userid);
   public List<UserChatHistoryDataObjects> getUsersChatHistoryList(Long userid,Long mappeduserid);
   public List<ChatUsersMaster> getUsersMappedList(Long userid);
}
