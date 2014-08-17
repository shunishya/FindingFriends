package com.findingfriends.interfaces;

import com.findingfriends.models.ContactModel;
import com.findingfriends.utils.ContactListSelectableItem;

public interface ContactAttachDetachListner {
	public void clickedUser(ContactListSelectableItem contact);

	public void contactRemoved(ContactModel mContact);
}
