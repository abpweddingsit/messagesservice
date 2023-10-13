package com.message.communication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.message.communication.entity.ChatUsersMaster;

public interface ChatUsersRepository extends JpaRepository<ChatUsersMaster, Long>{
   public List<ChatUsersMaster> findByUserid(Long userid);
}
