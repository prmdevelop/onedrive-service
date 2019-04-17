package com.ffi.onedriveservice.controller;

import java.io.Serializable;
import java.util.Map;

public class OneDriveResponseObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> oneDriveResponse;

	public Map<String, Object> getOneDriveResponse() {
		return oneDriveResponse;
	}

	public void setOneDriveResponse(Map<String, Object> oneDriveResponse) {
		this.oneDriveResponse = oneDriveResponse;
	}
}
