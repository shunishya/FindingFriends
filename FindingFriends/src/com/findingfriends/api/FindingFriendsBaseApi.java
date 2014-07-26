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

import com.findingfriends.utils.JsonUtil;

public class FindingFriendsBaseApi {
	HttpClient client = new DefaultHttpClient();
	String register_url = "http://192.168.1.106:8080/Finding_Friends_Server/webresources/register";
	String sync_contact_url="http://192.168.1.106:8080/Finding_Friends_Server/webresources/synccontact";

	public String getData() throws FindingFriendsException {
		// Prepare a request object
		HttpGet httpget = new HttpGet(register_url);
		InputStream instream;

		// Execute the request
		HttpResponse response;
		String result = null;
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
				result = JsonUtil.writeInputStreamAsString(instream);
				// now you have the string representation of the HTML request
				instream.close();
				return result;
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

	public InputStream postData(String data,String url) throws FindingFriendsException {
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
