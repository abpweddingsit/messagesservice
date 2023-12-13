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
@Table(name="user_device_info", schema = "chatservice")
@NamedQuery(name="UserDeviceInfo.findAll", query="SELECT a FROM UserDeviceInfo a")
public class UserDeviceInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "deviceinfoid",nullable = false)
	private Long deviceinfoid;
	@Column(name = "usercode")
	private String usercode;
	@Column(name = "userid")
	private Long userid;
	@Column(name = "platform")
	private String platform;
	@Column(name = "appversion")
	private String appversion;
	@Column(name = "buildversion")
	private String buildversion;
	@Column(name = "osversion")
	private String osversion;
	@Column(name = "devicename")
	private String devicename;
	@Column(name = "bundleid")
	private String bundleid;
	@Column(name = "pushtoken")
	private String pushtoken;

}
