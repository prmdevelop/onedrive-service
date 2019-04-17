package com.ffi.onedriveservice.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.ffi.onedriveservice.exception.ApplicationBusinessException;



public final class AccessTokenProvider {

	private static final Logger logger = LogManager.getLogger(AuthenticationProvider.class);

	private static final String POST = "POST";
	private static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String SCOPE_MS_GRAPH = "offline_access openid https://graph.microsoft.com/user.read https://graph.microsoft.com/files.read.all https://graph.microsoft.com/files.readwrite.all";
	private static final String GRANT_TYPE_CLIENT_CREDENTIALS = "password";
	private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	private static final String RESOURCE_REDIRECT_URI = "http://localhost:8080/";
	private static final String RESOURCE_MS_GRAPH = "https://graph.microsoft.com/";
	private static final String OAUTH2_TOKEN_URL_PREFIX = "https://login.microsoftonline.com/";
	private static final String OAUTH2_TOKEN_URL_SUFFIX = "/oauth2/token";

	private static final String PARAMETER_SCOPE = "scope";
	private static final String PARAMETER_CLIENT_SECRET = "client_secret";
	private static final String PARAMETER_GRANT_TYPE = "grant_type";
	private static final String PARAMETER_CLIENT_ID = "client_id";
	private static final String PARAMETER_RESOURCE = "resource";
	private static final String PARAMETER_USERNAME = "username";
	private static final String PARAMETER_PASSWORD = "password";
	private static final String PARAMETER_REDIRECT_URI = "redirect_uri";
	private static final String PARAMETER_REFRESH_TOKEN = "refresh_token";

	private final String tenantName;
	private final String clientId;
	private final String clientSecret;
	private final String userName;
	private final String password;

	private long expiresIn;
	private String accessToken;
	private String refreshToken;

	public AccessTokenProvider(String tenantName, String clientId, String clientSecret, String userName,
			String password) {
		this.tenantName = tenantName;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.userName = userName;
		this.password = password;
	}

	public HashMap<String, Object> getAccessToken(String token) throws ApplicationBusinessException {
		logger.info("Start of AccessTokenProvider.getAccessToken(String request)");
		HashMap<String, Object> cachedObject = new HashMap<String, Object>();
		HttpsURLConnection con = null;
		try {
			StringBuilder responseStrBuilder = new StringBuilder();
			StringBuilder params = new StringBuilder();
			add(params, PARAMETER_SCOPE, SCOPE_MS_GRAPH);
			add(params, PARAMETER_CLIENT_ID, clientId);
			add(params, PARAMETER_CLIENT_SECRET, clientSecret);
			add(params, PARAMETER_REDIRECT_URI, RESOURCE_REDIRECT_URI);
			if (token.equalsIgnoreCase("accessToken")) {
				add(params, PARAMETER_GRANT_TYPE, GRANT_TYPE_CLIENT_CREDENTIALS);
				add(params, PARAMETER_USERNAME, userName);
				add(params, PARAMETER_PASSWORD, password);
				add(params, PARAMETER_RESOURCE, RESOURCE_MS_GRAPH);
			} else {
				add(params, PARAMETER_GRANT_TYPE, GRANT_TYPE_REFRESH_TOKEN);
				add(params, PARAMETER_REFRESH_TOKEN, token);
			}
			System.out.println(tenantName+"'"+clientId+"'"+clientSecret+"'"+userName+"'"+password);
			byte[] data = params.toString().getBytes("UTF-8");
			URL url = new URL(OAUTH2_TOKEN_URL_PREFIX + tenantName + OAUTH2_TOKEN_URL_SUFFIX);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod(POST);
			con.setRequestProperty(CONTENT_TYPE, APPLICATION_FORM_URLENCODED);
			con.setDoOutput(true);
			con.setRequestProperty("Content-Length", String.valueOf(data.length));
			con.setConnectTimeout(5 * 1000);
			OutputStream outStream = con.getOutputStream();
			outStream.write(data);
			outStream.flush();
			outStream.close();
			BufferedReader br = null;
			if (con.getResponseCode() != 200) {
				throw new RuntimeException("Response code=" + con.getResponseCode());
			} else {
				br = new BufferedReader(new InputStreamReader((con.getInputStream())));
			}
			String line;
			while ((line = br.readLine()) != null) {
				responseStrBuilder.append(line);
			}
			br.close();
			JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
			accessToken = jsonObject.getString("access_token");
			expiresIn = jsonObject.getLong("expires_in");
			refreshToken = jsonObject.getString("refresh_token");
			cachedObject.put("access_Token", accessToken);
			cachedObject.put("refresh_Token", refreshToken);
			cachedObject.put("expires_in",TimeUnit.SECONDS.toMillis(expiresIn));
		} catch (Exception e) {
			logger.info("Error in AccessTokenProvider.getAccessToken(String request)");
			expiresIn = 0;
			accessToken = null;
			refreshToken = null;
			throw new ApplicationBusinessException(e.getMessage(), e);
		}finally {
			if(con != null){
				con.disconnect();
			}
		}
		logger.info("End of AccessTokenProvider.getAccessToken(String request)");
		return cachedObject;
	}

	private static void add(StringBuilder params, String key, String value) {
		if (params.length() > 0) {
			params.append("&");
		}
		params.append(key);
		params.append("=");
		try {
			params.append(URLEncoder.encode(value, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static Builder tenantName(String tenantName) {
		return new Builder(tenantName);
	}

	public static final class Builder {
		final String tenantName;
		String clientId;
		String clientSecret;
		String userName;
		String password;

		Builder(String tenantName) {
			this.tenantName = tenantName;
		}

		public Builder2 clientId(String clientId) {
			this.clientId = clientId;
			return new Builder2(this);
		}
	}

	public static final class Builder2 {
		private final Builder b;

		Builder2(Builder b) {
			this.b = b;
		}

		public Builder3 clientSecret(String clientSecret) {
			b.clientSecret = clientSecret;
			return new Builder3(b);
		}
	}

	public static final class Builder3 {
		private final Builder b;

		Builder3(Builder b) {
			this.b = b;
		}

		public Builder4 username(String userName) {
			b.userName = userName;
			return new Builder4(b);
		}
	}

	public static final class Builder4 {
		private final Builder b;

		Builder4(Builder b) {
			this.b = b;
		}

		public Builder4 password(String password) {
			b.password = password;
			return this;
		}

		public AccessTokenProvider build() {
			return new AccessTokenProvider(b.tenantName, b.clientId, b.clientSecret, b.userName, b.password);
		}
	}
}
