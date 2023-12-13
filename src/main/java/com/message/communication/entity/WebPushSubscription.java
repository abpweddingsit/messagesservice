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
@Table(name="web_push_subcription_info", schema = "chatservice")
@NamedQuery(name="WebPushSubscription.findAll", query="SELECT a FROM WebPushSubscription a")
public class WebPushSubscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "id",nullable = false)
	private Long id;
	@Column(name = "userid")
	private String userid;
	@Column(name = "endpoint")
	private String endpoint;
	@Column(name = "p256dh")
	private String p256dh;
	@Column(name = "auth")
	private String auth;

}
