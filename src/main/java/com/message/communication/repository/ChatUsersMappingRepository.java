package com.message.communication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.message.communication.dataobjects.UserChatHistoryDataObjects;
import com.message.communication.dataobjects.UserFriendListDataObjects;
import com.message.communication.dataobjects.UserFriendListMessageObject;
import com.message.communication.entity.ChatUsersMapping;



public interface ChatUsersMappingRepository extends JpaRepository<ChatUsersMapping, Long>{
   public List<ChatUsersMapping> findByUseridAndMappeduseridAndIsactive(Long userid,Long mappeduserid,Integer isActive);
   public List<ChatUsersMapping> findByUseridAndIsactive(Long userid,Integer isActive);
   public List<ChatUsersMapping> findByMappeduseridAndIsactive(Long mappeduserid,Integer isActive);
   
   @Query(value=" select \n"
   		+ "    b.userid ,b.mappeduserid ,b.modifyon,b.lastmessageid,b.unreadcount,a.vdoutboundisallowed  \n"
   		+ " from chat_users_master a, \n"
   		+ "     chat_users_mapping b \n"
   		+ " where a.userid=b.userid \n"
   		+ " and a.isactive =1 \n"
   		+ " and b.isactive =1 \n"
   		+ " and a.userid = :userid order by b.modifyon desc", nativeQuery = true)
   public List<UserFriendListDataObjects> getUsersFriendList(@Param("userid") Long userid);
   
   /*@Query(value=" select \n"
   		+ "   			 b.mappeduserid , b.createdon ,b.modifyon ,c.messagebody ,b.lastmessageid ,b.unreadcount, c.isreadmsg \n"
   		+ "   		  		from  \n"
   		+ "   		   		    chat_users_mapping b, \n"
   		+ "   		   		     chat_message_details c \n"
   		+ "   		   		 where b.userid=c.userid \n"
   		+ "   		   		 and b.lastmessageid = c.messageid \n"
   		+ "   		and (c.userid = :userid and c.mappeduserid = :mappeduserid) or (c.userid = :mappeduserid and c.mappeduserid = :userid) order by c.createdon  desc", nativeQuery = true)
	   public List<UserFriendListDataObjects> getUsersFriendListUpdateMsg(@Param("userid") Long userid,@Param("mappeduserid") Long mappeduserid);*/
   
   @Query(value=" SELECT x.userid,x.messagebody,x.mappeduserid,x.isreadmsg,x.createdon,x.calltype FROM chatservice.chat_message_details x \n"
   		+ "WHERE (x.userid =:userid and x.mappeduserid =:mappeduserid) or (x.userid =:mappeduserid and x.mappeduserid =:userid) \n"
   		+ "ORDER BY x.createdon DESC ", nativeQuery = true)
		   public List<UserFriendListMessageObject> getUsersFriendListUpdateMsg(@Param("userid") Long userid,@Param("mappeduserid") Long mappeduserid);
   
   
   /*@Query(value="  select c.* \n"
   		+ " from chat_message_details c left join chat_users_mapping b on \n"
   		+ " b.userid=c.userid and b.mappeduserid = c.mappeduserid \n"
   		+ " where b.isactive =1 \n"
   		+ " and b.userid = :userid and b.mappeduserid = :mappeduserid order by c.createdon  desc ", nativeQuery = true)
   public List<UserChatHistoryDataObjects> getUsersChatHistoryList(@Param("userid") Long userid,@Param("mappeduserid") Long mappeduserid);*/
   
   @Query(value="  select c.* \n"
   		+ " from chat_message_details c left join chat_users_mapping b on \n"
   		+ " b.userid=c.userid and b.mappeduserid = c.mappeduserid \n"
   		+ " where  (b.userid = :userid and b.mappeduserid = :mappeduserid) or (b.userid = :mappeduserid and b.mappeduserid = :userid) order by c.createdon  desc LIMIT :end OFFSET :start ", nativeQuery = true)
	   public List<UserChatHistoryDataObjects> getUsersChatHistoryList(@Param("userid") Long userid,@Param("mappeduserid") Long mappeduserid,@Param("start") Integer start,@Param("end") Integer end);
   
   /*@Query(value="  select c.* \n"
   		+ "from chat_message_details c left join chat_users_mapping b on \n"
   		+ "b.userid=c.userid and b.mappeduserid = c.mappeduserid \n"
   		+ "where b.isactive =1 \n"
   		+ "and b.userid = :userid and b.mappeduserid = :mappeduserid and c.createdon > :timestamp order by c.createdon  desc ", nativeQuery = true)
	   public List<UserChatHistoryDataObjects> getUsersChatHistoryListByTime(@Param("userid") Long userid,@Param("mappeduserid") Long mappeduserid,@Param("timestamp") Long timestamp);*/
   
   @Query(value="  select c.* \n"
   		+ "   		from chat_message_details c left join chat_users_mapping b on \n"
   		+ "   		b.userid=c.userid and b.mappeduserid = c.mappeduserid \n"
   		+ "   		where c.createdon > :timestamp and ((b.userid = :userid and b.mappeduserid = :mappeduserid) or (b.userid = :mappeduserid and b.mappeduserid = :userid)) order by c.createdon  desc  LIMIT :end OFFSET :start ", nativeQuery = true)
		   public List<UserChatHistoryDataObjects> getUsersChatHistoryListByTime(@Param("userid") Long userid,@Param("mappeduserid") Long mappeduserid,@Param("timestamp") Long timestamp,@Param("start") Integer start,@Param("end") Integer end);
}
