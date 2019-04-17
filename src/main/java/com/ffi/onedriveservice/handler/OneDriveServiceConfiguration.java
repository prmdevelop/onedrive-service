package com.ffi.onedriveservice.handler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@ConfigurationProperties("onedrive-service")
public class OneDriveServiceConfiguration {

	private Success success;
	private Error error;
	
	public static class Success {

		private String code;
		private String retrieve;
		private String delete;
		private String modify;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getRetrieve() {
			return retrieve;
		}

		public void setRetrieve(String retrieve) {
			this.retrieve = retrieve;
		}

		public String getDelete() {
			return delete;
		}

		public void setDelete(String delete) {
			this.delete = delete;
		}

		public String getModify() {
			return modify;
		}

		public void setModify(String modify) {
			this.modify = modify;
		}
	}

	public static class Error {

		private String record;
		private String code;
		private String retrieve;
		private String delete;
		private String modify;

		public String getRecord() {
			return record;
		}

		public void setRecord(String record) {
			this.record = record;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getRetrieve() {
			return retrieve;
		}

		public void setRetrieve(String retrieve) {
			this.retrieve = retrieve;
		}

		public String getDelete() {
			return delete;
		}

		public void setDelete(String delete) {
			this.delete = delete;
		}

		public String getModify() {
			return modify;
		}

		public void setModify(String modify) {
			this.modify = modify;
		}
	}
	
	public Success getSuccess() {
		return success;
	}

	public void setSuccess(Success success) {
		this.success = success;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

}
