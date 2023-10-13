package com.message.communication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.message.communication.entity.ChatMessageDetails;




public interface ChatMessageDetailsRepository extends JpaRepository<ChatMessageDetails, Long>{
	public List<ChatMessageDetails> findByUseridAndMappeduseridOrderByCreatedonDesc(Long userid,Long mappeduserid);
}
