package com.ffi.onedriveservice.service;

public interface IOneDrive {
	
	public String accessToken();
	public String uploadFile(String filePath);
	public void downloadFile(String itemId);
}
