package com.findingfriends.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FindingFriendsPreferences {
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor mEditor;

	public static final String PREFS_NAME = "FindingFriendsPreference";
	public static final String USER_ID = "user_id";
	public static final String PHONE_NUMBER = "phone_number";

	public FindingFriendsPreferences(Context context) {
		mSharedPreferences = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

	}

	public void setUserID(String user_id) {
		mEditor = mSharedPreferences.edit();
		mEditor.putString(USER_ID, user_id);
		mEditor.commit();
	}

	public String getUserID() {
		return mSharedPreferences.getString(USER_ID, null);
	}

	public void setPhoneNumber(String phoneNumber) {
		mEditor = mSharedPreferences.edit();
		mEditor.putString(PHONE_NUMBER, phoneNumber);
		mEditor.commit();
	}

	public String getPhoneNumber() {
		return mSharedPreferences.getString(PHONE_NUMBER, null);
	}

	public boolean isUserLoggedIn() {
		if (getUserID() != null)
			return true;
		else
			return false;
		// return true;
	}

}
