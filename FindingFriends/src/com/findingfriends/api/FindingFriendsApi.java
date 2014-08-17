package com.findingfriends.api;

import java.io.InputStream;

import android.content.Context;

import com.findingfriends.api.models.ContactSyncRequest;
import com.findingfriends.api.models.GroupOfFriendRequest;
import com.findingfriends.api.models.GroupOfFriendsResponse;
import com.findingfriends.api.models.NearestFriendRequest;
import com.findingfriends.api.models.NearestFriendResponse;
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
		InputStream res = postData(JsonUtil.writeValue(request), REGISTER_URL);
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
				SYNC_CONTACT_URL);
		if (res != null) {
			contactSyncResponse = (SyncContactResponse) JsonUtil.readJson(res,
					SyncContactResponse.class);
			return contactSyncResponse;
		} else {
			throw new FindingFriendsException(
					"ILoopException: Failed to get response");
		}

	}

	public NearestFriendResponse getNearestFriends(NearestFriendRequest request)
			throws FindingFriendsException {
		NearestFriendResponse nearestFriendResponse = new NearestFriendResponse();
		InputStream res = postData(JsonUtil.writeValue(request),
				NEAREST_FRIEND_REQUEST_URL);
		if (res != null) {
			nearestFriendResponse = (NearestFriendResponse) JsonUtil.readJson(
					res, NearestFriendResponse.class);
			return nearestFriendResponse;
		} else {
			throw new FindingFriendsException(
					"ILoopException: Failed to get response");
		}

	}

	public GroupOfFriendsResponse findGroupOFFriends(
			GroupOfFriendRequest request) throws FindingFriendsException {
		GroupOfFriendsResponse findGroupOFFriendResponse = new GroupOfFriendsResponse();
		InputStream res = postData(JsonUtil.writeValue(request),
				FIND_GROUP_OF_FRIEND_REQUEST_URL);
		if (res != null) {
			findGroupOFFriendResponse = (GroupOfFriendsResponse) JsonUtil
					.readJson(res, GroupOfFriendsResponse.class);
			return findGroupOFFriendResponse;
		} else {
			throw new FindingFriendsException(
					"ILoopException: Failed to get response");
		}

	}

}
