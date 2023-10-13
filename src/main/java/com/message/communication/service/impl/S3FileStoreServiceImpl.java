package com.message.communication.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.message.communication.service.S3FileStoreService;
import com.message.communication.util.MimeMap;
import com.message.communication.util.S3Config;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class S3FileStoreServiceImpl implements S3FileStoreService{
	private static final Logger logger = LoggerFactory.getLogger(S3FileStoreServiceImpl.class);
	
	@Value("${amazon.s3.bucket}")
    private String bucketName;
	
	@Value("${amazon.s3.accessKey}")
    private String accessKey ;
	
	@Value("${amazon.s3.secretKey}")
    private String secretKey ;
	
	@Value("${cloud.aws.region.static}")
    private String regionName;
	
	
	/*@Autowired
	private AmazonS3 s3Client;*/
	
    /*public S3FileStoreServiceImpl() {
    	
    	AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder.standard();
    	
		RetryPolicy retryPolicy = new RetryPolicy(null, null, 3, true);
		
		ClientConfiguration clientConfiguration = new ClientConfiguration()
         .withConnectionTimeout(360000) 
         .withSocketTimeout(360000)
         .withMaxConnections(100)
         .withRetryPolicy(retryPolicy);
    	 
    	AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
		Regions region = Regions.AP_SOUTH_1;
		s3Client = amazonS3ClientBuilder
				.withRegion(region)
				.withCredentials(credentialsProvider)
				.withClientConfiguration(clientConfiguration)
				.build();
	}*/
	
	public String uploadS3object(MultipartFile file, String title,String folder, String fileName) {
		
		ObjectMetadata objectMetadata = new ObjectMetadata();
		String contentType = file.getContentType();
		String extention = MimeMap.getExtention(contentType);
		//String fileName = Long.toString(new Date().getTime())+"."+extention;
		fileName = fileName+"."+extention;
		String s3Key = getS3Key(folder, fileName);
		logger.info("contentType.."+contentType);
		logger.info("extention.."+extention);
		logger.info("s3Key.."+s3Key);
		
		logger.info("bucketName.."+bucketName);
		
		
		try {
			
			AmazonS3 s3Client = s3client();
			objectMetadata.setContentType(contentType);
			objectMetadata.setContentLength(file.getSize());
			PutObjectRequest p = new PutObjectRequest(bucketName, s3Key, file.getInputStream(), objectMetadata);
			p.withCannedAcl(CannedAccessControlList.PublicRead);
			
			s3Client.putObject(p);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("Error while uploading file to s3 1");
			logger.error(e.getMessage());
		}
		
		
		return fileName;
	}
	
	public String uploadWebpToS3(MultipartFile file, String title,String folder, String fileName) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		fileName = fileName+".webp";
		String webps3Key = getS3Key(folder, fileName);
		try {
			
			AmazonS3 s3Client = s3client();
			// Obtain an image to encode from somewhere
			BufferedImage imageio = ImageIO.read(convertMultiPartToFile(file));
			// Encode it as webp using default settings
			ImageIO.write(imageio, "webp", convertMultiPartToFile(file));
			objectMetadata.setContentType("image/webp");
			objectMetadata.setContentLength(file.getSize());
			PutObjectRequest p1 = new PutObjectRequest(bucketName, webps3Key, file.getInputStream(), objectMetadata);
			p1.withCannedAcl(CannedAccessControlList.PublicRead);
			s3Client.putObject(p1);
		} catch (IOException e) {
			logger.error("Error while uploading webp file to s3 2");
			logger.error(e.getMessage());
		}
		return fileName;
	}
	
	public String resizeUploadS3object(MultipartFile file, String title,String folder, int widthInt, String fileName) {
		InputStream inputStream = null;
		Long size = 0l;
		String contentType = file.getContentType();
		String extention = MimeMap.getExtention(contentType);
		try {
			if (widthInt > 0) {
				inputStream = resizeImageInputStream(file.getInputStream(),
						widthInt, extention);
				size  = (long) inputStream.available();
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		folder = "w"+widthInt+"/"+folder;
		return uploadS3object(inputStream, title, folder, contentType , size, fileName);
	}
	
    private String uploadS3object(InputStream file, String title, String folder, String contentType, long size, String fileName) {
		
		ObjectMetadata objectMetadata = new ObjectMetadata();
		//String contentType = file.getContentType();
		String extention = MimeMap.getExtention(contentType);
		//String fileName = Long.toString(new Date().getTime())+"."+extention;
		fileName = fileName+"."+extention;
		String s3Key = getS3Key(folder, fileName);
		try {
			
			AmazonS3 s3Client = s3client();
			
			objectMetadata.setContentType(contentType);
			objectMetadata.setContentLength(size);
			PutObjectRequest p = new PutObjectRequest(bucketName, s3Key, file, objectMetadata);
			p.withCannedAcl(CannedAccessControlList.PublicRead);
			s3Client.putObject(p);
		} catch (Exception e) {
			logger.error("Error while uploading file to s3 3");
			logger.error(e.getMessage());
		}
		
		
		return fileName;
	}
	
	private String getS3Key(String folder, String fileName){
		StringBuilder s3Key = new StringBuilder();
		if(folder !=null){
			s3Key.append(folder);
		}
		s3Key.append(fileName);
		return s3Key.toString();
	}
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
	
	private InputStream resizeImageInputStream(InputStream fileInputStream, int width, String extention) throws IOException {
		BufferedImage originalImage = ImageIO.read(fileInputStream);
		InputStream finalFileInputStream = fileInputStream;
		if(originalImage.getWidth() >= width){
			finalFileInputStream = resizeImage(originalImage , width, extention);
		}
		return finalFileInputStream;
	}
	
	private InputStream resizeImage(BufferedImage imgFile, int width, String extention) throws FileNotFoundException, IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Thumbnails.of(imgFile)
		        .size(width, width)
		        .outputFormat(extention)
		        .toOutputStream(os);
		return new ByteArrayInputStream(os.toByteArray());
	}
	
	private AmazonS3 s3client() {
    	logger.info("accessKey.."+accessKey);
    	logger.info("secretKey.."+secretKey);
    	
    	BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
    	AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
    	//AWSCredentialsProvider credentialsProvider = 
		return AmazonS3ClientBuilder
				.standard()
				.withCredentials(credentialsProvider)
				.withRegion(regionName)
				.build();
	}

}
