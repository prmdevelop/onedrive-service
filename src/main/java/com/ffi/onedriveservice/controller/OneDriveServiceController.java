package com.ffi.onedriveservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ffi.onedriveservice.handler.OneDriveServiceConfiguration;
import com.ffi.onedriveservice.service.AuthenticationProvider;
import com.ffi.onedriveservice.service.IOneDrive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "OneDrive End Point")
@RestController
@RequestMapping("/onedrive/service")
public class OneDriveServiceController {

	private static final Logger logger = LogManager.getLogger(OneDriveServiceController.class);

	@Autowired
	AuthenticationProvider authenticationProvider;
	
	@Autowired
	IOneDrive oneDrive;

	@Autowired
	OneDriveServiceConfiguration configuration;

	@ApiOperation(value = "Create File")
	@PostMapping(value = "/createFile", produces = "application/json")
	public OneDriveResponseJson<OneDriveResponseObject> uploadFile(@RequestParam("filePath") final String filePath) {
		logger.info("Start of OneDriveEndpoint.uploadFile()");
		OneDriveResponseJson<OneDriveResponseObject> responseJson = new OneDriveResponseJson<>();
		try {
			OneDriveResponseObject response = new OneDriveResponseObject();
			Map<String, Object> responseObject = new HashMap<>();
			responseObject.put("URL",oneDrive.uploadFile(filePath));
			responseObject.put("accessToken", oneDrive.accessToken());
			response.setOneDriveResponse(responseObject);
			responseJson.setData(response);
			responseJson.setStatusMessage(configuration.getSuccess().getRetrieve());
			responseJson.setStatusCode(configuration.getSuccess().getCode());
		} catch (Exception e) {
			logger.info("Error in OneDriveEndpoint.uploadFile()");
			responseJson.setErrorCode(configuration.getError().getRetrieve());
			responseJson.setErrorMessage(configuration.getError().getCode());
		}
		logger.info("End of OneDriveEndpoint.uploadFile()");
		return responseJson;
	}
}
