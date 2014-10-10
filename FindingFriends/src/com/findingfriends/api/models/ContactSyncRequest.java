package com.findingfriends.api.models;

import com.findingfriends.models.ContactModel;

import java.util.List;

public class ContactSyncRequest {
	private String user_id;
	private List<ContactModel> contactsTobeAdd;
	private List<String> contactsToBeDeleted;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public List<ContactModel> getContactsTobeAdd() {
		return contactsTobeAdd;
	}

	public void setContactsTobeAdd(List<ContactModel> contactsTobeAdd) {
		this.contactsTobeAdd = contactsTobeAdd;
	}

	public List<String> getContactsToBeDeleted() {
		return contactsToBeDeleted;
	}

	public void setContactsToBeDeleted(List<String> contactsToBeDeleted) {
		this.contactsToBeDeleted = contactsToBeDeleted;
	}

}
