package com.ffi.onedriveservice.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ffi.onedriveservice.exception.ApplicationBusinessException;
import com.microsoft.graph.concurrency.ChunkedUploadProvider;
import com.microsoft.graph.concurrency.IProgressCallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.extensions.DriveItem;
import com.microsoft.graph.models.extensions.DriveItemUploadableProperties;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.UploadSession;

@Service
public class OneDriveServiceImpl implements IOneDrive {
	
	@Autowired
	AuthenticationProvider authenticationProvider;
	
	protected IGraphServiceClient graphServiceClient = null;
	protected String webURL = null;
	
	@Override
	public String accessToken() {	
		String accessToken="";
		try {
			accessToken = authenticationProvider.accessToken();
		} catch (ApplicationBusinessException e) {
			e.printStackTrace();
		}
		return accessToken;
	}

	@Override
	public String uploadFile(String filePath) {
		try (InputStream uploadFile = new FileInputStream(filePath)){
			if(null == graphServiceClient){
				graphServiceClient = authenticationProvider.createMsGraphClient();
			}
			
			int fileSize = uploadFile.available();
			IProgressCallback<DriveItem> callback = new IProgressCallback<DriveItem>() {
				@Override
				public void progress(final long current, final long max) {
					// Check progress
				}

				@Override
				public void success(final DriveItem result) {
					// Handle the successful response
					sendwebURL(result);
				}

				@Override
				public void failure(final ClientException ex) {
					// Handle the failed upload
				}
			};

			UploadSession uploadSession = graphServiceClient.me().drive().root().itemWithPath("SME_Template_v0.21_updated.xlsx")
					.createUploadSession(new DriveItemUploadableProperties()).buildRequest().post();
			ChunkedUploadProvider<DriveItem> chunkedUploadProvider = new ChunkedUploadProvider<>(uploadSession,
					graphServiceClient, uploadFile, fileSize, DriveItem.class);
			chunkedUploadProvider.upload(callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webURL;
	}
	
	public void sendwebURL(DriveItem result){
		webURL = result.webUrl;
	}

	@Override
	public void downloadFile(String itemId) {
		try {
			if(null == graphServiceClient){
				graphServiceClient = authenticationProvider.createMsGraphClient();
			}
			InputStream in = graphServiceClient.me().drive().items().byId(itemId).content().buildRequest().get();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
