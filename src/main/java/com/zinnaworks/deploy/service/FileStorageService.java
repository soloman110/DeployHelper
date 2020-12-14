package com.zinnaworks.deploy.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.JSchException;

public interface FileStorageService {

	String storeFile(MultipartFile file);

	Resource loadFileAsResource(String fileName);

	boolean sftp(String ip, String userName, String ps, String remoteBaseDir, String fileName, String localFilePath)
			throws JSchException;
	
	String getUploadDir();

}