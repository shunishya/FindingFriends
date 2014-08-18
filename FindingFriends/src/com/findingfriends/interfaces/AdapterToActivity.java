package com.findingfriends.interfaces;

import com.findingfriends.models.UserWithDistance;

public interface AdapterToActivity {
	public void navigate(UserWithDistance user);

	public void makeACall(String phoneNumber);

	public void sendMsg(String phoneNumber);
}
