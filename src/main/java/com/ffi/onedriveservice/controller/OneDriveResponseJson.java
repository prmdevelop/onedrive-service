package com.ffi.onedriveservice.controller;

public class OneDriveResponseJson<T> {

	private String statusCode;
	private String statusMessage;
	private String errorCode;
	private String errorMessage;
	private T data;

	public OneDriveResponseJson() {
	}

	public OneDriveResponseJson(String statusCode, String statusMessage, String errorCode, String errorMessage, T data) {
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.data = data;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "OneDriveResponseJson [statusCode=" + statusCode + ", statusMessage=" + statusMessage + ", errorCode="
				+ errorCode + ", errorMessage=" + errorMessage + ", data=" + data + "]";
	}

}
