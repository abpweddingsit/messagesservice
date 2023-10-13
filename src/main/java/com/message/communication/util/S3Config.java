package com.message.communication.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


@Configuration
public class S3Config {
	private static final Logger logger = LoggerFactory.getLogger(S3Config.class);
	@Value("${amazon.s3.accessKey}")
    private String awsId;

    @Value("${amazon.s3.secretKey}")
    private String awsKey;

    @Value("${cloud.aws.region.static}")
    private String regionName;
    
    public AmazonS3 s3client() {
    	logger.info("awsId.."+awsId);
    	logger.info("awsKey.."+awsKey);
    	
    	BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsId, awsKey);
    	AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
    	//AWSCredentialsProvider credentialsProvider = 
		return AmazonS3ClientBuilder
				.standard()
				.withCredentials(credentialsProvider)
				.withRegion(regionName)
				.build();
	}
}
