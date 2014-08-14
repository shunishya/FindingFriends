package com.findingfriends.utils;

import com.findingfriends.models.ContactModel;

public class ContactListSelectableItem {
	private boolean isSelected;
	private ContactModel mContact;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public ContactModel getContact() {
		return mContact;
	}

	public void setContact(ContactModel mImnContact) {
		this.mContact = mImnContact;
	}

}
