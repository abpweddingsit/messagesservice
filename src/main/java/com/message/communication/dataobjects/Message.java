package com.message.communication.dataobjects;



import lombok.Data;

@Data
public class Message {

	private String senderName;
    private String targetUserName;
    private String message;
    private String type;

}
