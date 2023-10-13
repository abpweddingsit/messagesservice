package com.message.communication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Table(name="chat_message_details", schema = "chatservice")
@NamedQuery(name="ChatMessageDetails.findAll", query="SELECT a FROM ChatMessageDetails a")
public class ChatMessageDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "messageid",nullable = false)
	private Long messageid;
	@Column(name = "userid")
	private Long userid;
	@Column(name = "messagebody")
	private String messagebody;
	@Column(name = "mappeduserid")
	private Long mappeduserid;
	@Column(name = "isreadmsg")
	private Integer isreadmsg;
	@Column(name = "createdon")
	private Long createdon;
	
}
