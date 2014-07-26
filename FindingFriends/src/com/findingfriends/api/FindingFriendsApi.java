package com.findingfriends.api;

import java.io.InputStream;

import android.content.Context;

import com.findingfriends.api.models.ContactSyncRequest;
import com.findingfriends.api.models.RegisterRequest;
import com.findingfriends.api.models.RegisterResponse;
import com.findingfriends.api.models.SyncContactResponse;
import com.findingfriends.utils.JsonUtil;

public class FindingFriendsApi extends FindingFriendsBaseApi {
	Context mContext;

	public FindingFriendsApi(Context context) {

		this.mContext = context;
	}

	public RegisterResponse sendRegisterRequest(RegisterRequest request)
			throws FindingFriendsException {
		RegisterResponse response = new RegisterResponse();
		InputStream res = postData(JsonUtil.writeValue(request), register_url);
		if (res != null) {
			response = (RegisterResponse) JsonUtil.readJson(res,
					RegisterResponse.class);
			if (response.isError()) {
				throw new FindingFriendsException("ILoopException:"
						+ response.getMessage());
			}
		} else {
			response.setError(true);
		}
		return response;
	}

	public SyncContactResponse contactSync(ContactSyncRequest syncRequest)
			throws FindingFriendsException {
		SyncContactResponse contactSyncResponse = new SyncContactResponse();
		InputStream res = postData(JsonUtil.writeValue(syncRequest),
				sync_contact_url);
		if (res != null) {
			contactSyncResponse = (SyncContactResponse) JsonUtil.readJson(res,
					SyncContactResponse.class);
			return contactSyncResponse;
		} else {
			throw new FindingFriendsException(
					"ILoopException: Failed to get response");
		}

	}

}
