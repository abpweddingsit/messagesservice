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
@Table(name="chat_users_mapping", schema = "chatservice")
@NamedQuery(name="ChatUsersMapping.findAll", query="SELECT a FROM ChatUsersMapping a")
public class ChatUsersMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "chatusermappingid",nullable = false)
	private Long chatusermappingid;
	@Column(name = "userid")
	private Long userid;
	@Column(name = "mappeduserid")
	private Long mappeduserid;
	@Column(name = "isactive")
	private Integer isactive;
	@Column(name = "createdon")
	private Long createdon;
	@Column(name = "modifyon")
	private Long modifyon;
	@Column(name = "lastmessageid")
	private Long lastmessageid;
	@Column(name = "unreadcount")
	private Integer unreadcount;
	
	
	
}
