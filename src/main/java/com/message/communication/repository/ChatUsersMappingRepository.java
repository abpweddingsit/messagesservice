package com.message.communication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.entity.ChatUsersMapping;



public interface ChatUsersMappingRepository extends JpaRepository<ChatUsersMapping, Long>{
   public List<ChatUsersMapping> findByUseridAndMappeduseridAndIsactive(Long userid,Long mappeduserid,Integer isActive);
   
   @Query(value=" select \n"
   		+ "     a.usercode ,b.mappeduserid ,b.createdon ,b.modifyon ,c.messagebody ,b.lastmessageid ,b.unreadcount \n"
   		+ " from chat_users_master a, \n"
   		+ "     chat_users_mapping b, \n"
   		+ "     chat_message_details c \n"
   		+ " where a.userid=b.userid \n"
   		+ " and b.userid=c.userid \n"
   		+ " and b.lastmessageid = c.messageid \n"
   		+ " and a.isactive =1 \n"
   		+ " and a.userid = :userid order by c.createdon desc", nativeQuery = true)
   public List<UserFriendListDataObjects> getUsersFriendList(@Param("userid") Long userid);
   
   
   @Query(value="  select c.* \n"
   		+ " from chat_message_details c left join chat_users_mapping b on \n"
   		+ " b.userid=c.userid and b.mappeduserid = c.mappeduserid \n"
   		+ " where b.isactive =1 \n"
   		+ " and b.userid = :userid and b.mappeduserid = :mappeduserid order by c.createdon  asc ", nativeQuery = true)
   public List<UserChatHistoryDataObjects> getUsersChatHistoryList(@Param("userid") Long userid,@Param("mappeduserid") Long mappeduserid);
}
