package com.findingfriends.api;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class FindingFriendsBaseApi {
	HttpClient client = new DefaultHttpClient();
	// public static String BASE_URL =
	// "http://10.0.2.14:8080/Finding_Friends_Server/webresources/";

	public static String BASE_URL = "http://192.168.1.104:8080/Finding_Friends_Server/webresources/";
	// public static String BASE_URL =
	// "http://finding-friendsandfamily.rhcloud.com/webresources/";
	public static String REGISTER_URL = BASE_URL + "register";
	public static String SYNC_CONTACT_URL = BASE_URL + "synccontact";
	public static String NEAREST_FRIEND_REQUEST_URL = BASE_URL
			+ "nearestfriends";
	public static String FIND_GROUP_OF_FRIEND_REQUEST_URL = BASE_URL
			+ "nearestfriends/findgroupoffriends";
	public static String UPDATE_INFO = BASE_URL + "update";

	public InputStream getData() throws FindingFriendsException {
		// Prepare a request object
		HttpGet httpget = new HttpGet(REGISTER_URL);
		InputStream instream;

		// Execute the request
		HttpResponse response;

		try {
			response = client.execute(httpget);

			int httpStatusCode = response.getStatusLine().getStatusCode();
			switch (httpStatusCode) {
			case 200:
				// Get hold of the response entity
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
				// to worry about connection release
				instream = entity.getContent();
				return instream;
			case 400:
				throw new FindingFriendsException("Bad Request 400");
			case 401:
				throw new FindingFriendsException("Authentication Failed 401.");
			case 500:
				throw new FindingFriendsException("Internal Server Error 500");
			default:

				throw new FindingFriendsException("No results :"
						+ httpStatusCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new FindingFriendsException(
					"Failed to get response from server :" + e.getMessage());
		}

	}

	public InputStream postData(String data, String url)
			throws FindingFriendsException {
		// Prepare a request object
		HttpPost httpPost = new HttpPost(url);
		InputStream instream;
		httpPost.setHeader("Content-Type", "application/json");
		try {
			httpPost.setEntity(new StringEntity(data));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// Execute the request
		HttpResponse response;
		try {
			response = client.execute(httpPost);

			int httpStatusCode = response.getStatusLine().getStatusCode();
			switch (httpStatusCode) {
			case 200:
				// Get hold of the response entity
				HttpEntity entity = response.getEntity();
				// If the response does not enclose an entity, there is no need
				// to worry about connection release
				instream = entity.getContent();
				return instream;
			case 400:
				throw new FindingFriendsException("Bad Request 400");
			case 401:
				throw new FindingFriendsException("Authentication Failed 401.");
			case 500:
				throw new FindingFriendsException("Internal Server Error 500");
			default:
				throw new FindingFriendsException("No results");
			}
		} catch (Exception e) {
			throw new FindingFriendsException(
					"Failed to get response from server :" + e.getMessage());
		}

	}
}
