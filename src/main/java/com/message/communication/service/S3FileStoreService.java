package com.message.communication.service;

import org.springframework.web.multipart.MultipartFile;

public interface S3FileStoreService {
	public String uploadS3object(MultipartFile file, String title,String folder, String fileName);
	public String uploadWebpToS3(MultipartFile file, String title,String folder, String fileName);
	public String resizeUploadS3object(MultipartFile file, String title,String folder, int widthInt, String fileName);
}
