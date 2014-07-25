package com.findingfriends.api;

import java.io.InputStream;

import android.content.Context;

import com.findingfriends.api.models.RegisterRequest;
import com.findingfriends.api.models.RegisterResponse;
import com.findingfriends.utils.JsonUtil;

public class FindingFriendsApi extends FindingFriendsBaseApi {
	Context mContext;

	public FindingFriendsApi(Context context) {

		this.mContext = context;
	}

	public RegisterResponse sendRegisterRequest(RegisterRequest request) {
		RegisterResponse response = new RegisterResponse();
		InputStream res = postData(JsonUtil.writeValue(request));
		if (res != null) {
			response = (RegisterResponse) JsonUtil.readJson(res,
					RegisterResponse.class);
		} else {
			response.setError(true);
		}
		return response;
	}

}
