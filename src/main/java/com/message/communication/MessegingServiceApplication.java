package com.message.communication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MessegingServiceApplication {
	
	

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/messegingservice");
		SpringApplication.run(MessegingServiceApplication.class, args);
	}

}
