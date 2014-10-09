package com.findingfriends.api;

import android.content.Context;

import com.findingfriends.api.models.ContactSyncRequest;
import com.findingfriends.api.models.GroupOfFriendRequest;
import com.findingfriends.api.models.GroupOfFriendsResponse;
import com.findingfriends.api.models.NearestFriendRequest;
import com.findingfriends.api.models.NearestFriendResponse;
import com.findingfriends.api.models.RegisterRequest;
import com.findingfriends.api.models.RegisterResponse;
import com.findingfriends.api.models.SyncContactResponse;
import com.findingfriends.api.models.UpdateLocation;
import com.findingfriends.api.models.UpdateLocationResponse;
import com.findingfriends.utils.JsonUtil;

import java.io.InputStream;

public class FindingFriendsApi extends FindingFriendsBaseApi {
	Context mContext;

	public FindingFriendsApi(Context context) {

		this.mContext = context;
	}

	public RegisterResponse sendRegisterRequest(RegisterRequest request)
			throws FindingFriendsException {
		RegisterResponse response = new RegisterResponse();
		String data = JsonUtil.writeValue(request);
		InputStream res = postData(data, REGISTER_URL);
		if (res != null) {
			response = (RegisterResponse) JsonUtil.readJson(res,
					RegisterResponse.class);
			if (response.isError()) {
				throw new FindingFriendsException(response.getMessage());
			}
		} else {
			response.setError(true);
		}
		return response;
	}

	public SyncContactResponse contactSync(ContactSyncRequest syncRequest)
			throws FindingFriendsException {
		SyncContactResponse contactSyncResponse = new SyncContactResponse();
		String data = JsonUtil.writeValue(syncRequest);
		InputStream res = postData(data, SYNC_CONTACT_URL);
		if (res != null) {
			contactSyncResponse = (SyncContactResponse) JsonUtil.readJson(res,
					SyncContactResponse.class);
			return contactSyncResponse;
		} else {
			throw new FindingFriendsException("Failed to get response");
		}

	}

	public NearestFriendResponse getNearestFriends(NearestFriendRequest request)
			throws FindingFriendsException {
		NearestFriendResponse nearestFriendResponse = new NearestFriendResponse();
		String data = JsonUtil.writeValue(request);
		InputStream res = postData(data, NEAREST_FRIEND_REQUEST_URL);
		if (res != null) {
			nearestFriendResponse = (NearestFriendResponse) JsonUtil.readJson(
					res, NearestFriendResponse.class);
			return nearestFriendResponse;
		} else {
			throw new FindingFriendsException("Failed to get response");
		}

	}

	public GroupOfFriendsResponse findGroupOFFriends(
			GroupOfFriendRequest request) throws FindingFriendsException {
		GroupOfFriendsResponse findGroupOFFriendResponse = new GroupOfFriendsResponse();
		String data = JsonUtil.writeValue(request);
		InputStream res = postData(data, FIND_GROUP_OF_FRIEND_REQUEST_URL);
		if (res != null) {
			findGroupOFFriendResponse = (GroupOfFriendsResponse) JsonUtil
					.readJson(res, GroupOfFriendsResponse.class);
			return findGroupOFFriendResponse;
		} else {
			throw new FindingFriendsException("Failed to get response");
		}

	}

	public UpdateLocationResponse updateLocation(UpdateLocation request)
			throws FindingFriendsException {
		UpdateLocationResponse updateRequestResponse = new UpdateLocationResponse();
		String data = JsonUtil.writeValue(request);
		InputStream res = postData(data, UPDATE_INFO);
		if (res != null) {
			updateRequestResponse = (UpdateLocationResponse) JsonUtil.readJson(
					res, UpdateLocationResponse.class);
			return updateRequestResponse;
		} else {
			throw new FindingFriendsException("Failed to get response");
		}

	}

}
