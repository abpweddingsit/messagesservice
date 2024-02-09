package com.message.communication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Builder.Default;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Entity
@Table(name="chat_users_master", schema = "chatservice")
@NamedQuery(name="ChatUsersMaster.findAll", query="SELECT a FROM ChatUsersMaster a")
public class ChatUsersMaster {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Column(name = "chatusermasterid",nullable = false)
	private Long chatusermasterid;
	@Column(name = "userid")
	private Long userid;
	@Column(name = "usercode")
	private String usercode;
	@Column(name = "username")
	private String username;
	@Column(name = "userphotoimageurl")
	private String userphotoimageurl;
	@Column(name = "isactive")
	private Integer isactive;
	@Column(name = "createdon")
	private Long createdon;
	@Column(name = "modifyon")
	private Long modifyon;
	@Column(name = "vdoutboundisallowed")
	private Integer vdoutboundisallowed=0;
	@Column(name = "vdinboundisallowed")
	private Integer vdinboundisallowed=0;
	@Column(name = "vcallowedminiutes")
	private Long vcallowedminiutes=99999999999l;
	@Column(name = "vcconsumedminutes")
	private Long vcconsumedminutes=0l;
	@Column(name = "aucallowedminiutes")
	private Long aucallowedminiutes=99999999999l;
	@Column(name = "aucconsumedminutes")
	private Long aucconsumedminutes=0l;
}
