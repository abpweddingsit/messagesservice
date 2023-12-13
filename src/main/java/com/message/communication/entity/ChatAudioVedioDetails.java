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
@Table(name="chat_audiovedio_details", schema = "chatservice")
@NamedQuery(name="ChatAudioVedioDetails.findAll", query="SELECT a FROM ChatAudioVedioDetails a")
public class ChatAudioVedioDetails {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "audiovedioid",nullable = false)
	private Long audiovedioid;
	@Column(name = "userid")
	private Long userid;
	@Column(name = "mappeduserid")
	private Long mappeduserid;
	@Column(name = "inittime")
	private Long inittime;
	@Column(name = "starttime")
	private Long starttime;
	@Column(name = "callendtime")
	private Long callendtime;
	@Column(name = "calltype")
	private String calltype;
	@Column(name = "lengthofduration")
	private String lengthofduration;
	@Column(name = "callduration")
	private String callduration;
}
