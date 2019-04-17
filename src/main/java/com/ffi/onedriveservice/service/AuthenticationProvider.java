package com.ffi.onedriveservice.service;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ffi.onedriveservice.exception.ApplicationBusinessException;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

@Service
public class AuthenticationProvider {
	
	private static final Logger logger = LogManager.getLogger(AuthenticationProvider.class);
	
	protected IGraphServiceClient graphServiceClient = null;
	
	@Value("${tenantName}")
	private String tenantName;
	
	@Value("${clientId}")
	private String clientId;
	
	@Value("${clientSecret}")
	private String clientSecret;
	
	@Value("${user}")
	private String userName;
	
	@Value("${password}")
	private String password;
	
	private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
	private static final String OAUTH_BEARER_PREFIX = "Bearer ";
	
	HashMap<String, Object> cacheObject = new HashMap<String, Object>();

	
	public String accessToken() throws ApplicationBusinessException {
		logger.info("Start of OneDriveAuthenticationProvider.accessToken()");
		String accessToken = "";
		try {
			long now = System.currentTimeMillis();
			AccessTokenProvider accessTokenProvider = AccessTokenProvider.tenantName(tenantName)
					.clientId(clientId).clientSecret(clientSecret).username(userName).password(password).build();
			if (!(cacheObject.containsKey("access_Token") && cacheObject.containsKey("refresh_Token") && cacheObject.containsKey("expires_in"))) {
				//call access token method
				cacheObject = accessTokenProvider.getAccessToken("accessToken");
				cacheObject.put("firstCall", System.currentTimeMillis());
				accessToken = (String) cacheObject.get("access_Token");
			} else if (cacheObject.containsKey("access_Token") && cacheObject.get("access_Token") != null
					&& (long) cacheObject.get("expires_in") > now - (long) cacheObject.get("firstCall")) {
				//get access token from cache
				accessToken = (String) cacheObject.get("access_Token");
			} else {
				//call refresh token for access token;
				cacheObject = accessTokenProvider.getAccessToken((String) cacheObject.get("refresh_Token"));
				accessToken = (String) cacheObject.get("access_Token");
			}
		}catch(Exception e) {
			logger.info("Error in OneDriveAuthenticationProvider.createMsGraphClient()");
			e.printStackTrace();
			throw new ApplicationBusinessException(e.getMessage(), e);
		}
		return accessToken;
	}

	
	public IGraphServiceClient createMsGraphClient() throws ApplicationBusinessException {
		logger.info("Start of OneDriveAuthenticationProvider.createMsGraphClient()");
		String accessToken = "";
		try {
//			long now = System.currentTimeMillis();
//			AccessTokenProvider accessTokenProvider = AccessTokenProvider.tenantName(tenantName)
//					.clientId(clientId).clientSecret(clientSecret).username(userName).password(password).build();
//			if (!(cacheObject.containsKey("access_Token") && cacheObject.containsKey("refresh_Token") && cacheObject.containsKey("expires_in"))) {
//				//call access token method
//				cacheObject = accessTokenProvider.getAccessToken("accessToken");
//				cacheObject.put("firstCall", System.currentTimeMillis());
//				accessToken = (String) cacheObject.get("access_Token");
//			} else if (cacheObject.containsKey("access_Token") && cacheObject.get("access_Token") != null
//					&& (long) cacheObject.get("expires_in") > now - (long) cacheObject.get("firstCall")) {
//				//get access token from cache
//				accessToken = (String) cacheObject.get("access_Token");
//			} else {
//				//call refresh token for access token;
//				cacheObject = accessTokenProvider.getAccessToken((String) cacheObject.get("refresh_Token"));
//				accessToken = (String) cacheObject.get("access_Token");
//			}
			accessToken = accessToken();
		} catch (Exception e) {
			logger.info("Error in OneDriveAuthenticationProvider.createMsGraphClient()");
			e.printStackTrace();
			throw new ApplicationBusinessException(e.getMessage(), e);
			
		}
		IAuthenticationProvider authProvider = AuthenticationProvider.fromConfig(accessToken);
		logger.info("End of OneDriveAuthenticationProvider.createMsGraphClient()");
		return GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
	}
	
	
	
	private static IAuthenticationProvider fromConfig(String token) {
		IAuthenticationProvider authenticationProvider = new IAuthenticationProvider() {
			@Override
			public void authenticateRequest(IHttpRequest request) {
				// If the request already has an authorization header, do not intercept it.
				for (final HeaderOption option : request.getHeaders()) {
					if (option.getName().equals(AUTHORIZATION_HEADER_NAME)) {
						// Found an existing authorization header so don't add another
						return;
					}
				}
				try {
					request.addHeader(AUTHORIZATION_HEADER_NAME, OAUTH_BEARER_PREFIX + token);
				} catch (ClientException e) {
					final String message = "Unable to authenticate request, No active account found";
					throw new ClientException(message, e);
				}
			}
		};
		return authenticationProvider;
	}

}