package com.message.communication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.message.communication.entity.ChatAudioVedioDetails;




public interface ChatAudioVedioDetailsRepository extends JpaRepository<ChatAudioVedioDetails, Long>{
	public List<ChatAudioVedioDetails> findByUseridAndMappeduseridOrderByInittimeDesc(Long userid,Long mappeduserid);
}
