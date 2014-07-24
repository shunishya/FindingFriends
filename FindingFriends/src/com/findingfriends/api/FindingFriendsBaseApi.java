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
	String url = "http://192.168.1.106:8080/Finding_Friends_Server/webresources/register";

	public String getData() {
		// Prepare a request object
		HttpGet httpget = new HttpGet(url);

		// Execute the request
		HttpResponse response;
		String result = null;
		try {
			response = client.execute(httpget);

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				result = JsonUtil.writeInputStreamAsString(instream);
				// now you have the string representation of the HTML request
				instream.close();
			}
			return result;

		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
	}

	public InputStream postData(String data) {
		// Prepare a request object
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json");
		try {
			httpPost.setEntity(new StringEntity(data));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// Execute the request
		HttpResponse response;
		String result = null;
		try {
			response = client.execute(httpPost);

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				return instream;
			}
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
}
