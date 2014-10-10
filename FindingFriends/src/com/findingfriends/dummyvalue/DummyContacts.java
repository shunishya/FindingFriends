package com.findingfriends.dummyvalue;

import com.findingfriends.models.ContactModel;
import com.findingfriends.utils.ContactListSelectableItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DummyContacts {
	private static String[] contactName = { "Amit", "Ram", "Shyam", "Gita",
			"Sita", "Mita", "Lina", "Una", "Hari", "Laxman", "Suman", "Herman" };
	private static String phone = "9841123456";

	public static String[] dummyContact = { "Ram", "Shyam", "Hari", "John",
			"Sachin", "Subash", "Sudarshan", "Lina", "Dipak", "Mohan",
			"Sandeep", "Suman", "Jiten", "Ashok", "Prabin", "Rajan", "Indira",
			"Abhishek", "Julina", "Madan" };
	public static String[] dummyPhoneNumbers = { "9841123456", "9841234567",
			"9841345678", "9841456789", "9841567890", "9841678901",
			"9801234567", "9801234568", "9801234569", "9842123456",
			"9843123456", "9844123456", "9802345612", "9812345678",
			"9845123456", "9803344556", "98034125689", "9849123456",
			"9846345678", "9803012345" };

	public static ArrayList<ContactModel> getContacts() {
		ArrayList<ContactModel> listContacts = new ArrayList<ContactModel>();
		for (int i = 0; i < contactName.length; i++) {
			ContactModel model = new ContactModel();
			model.setName(contactName[i]);
			model.setPhonenumber(phone);
			listContacts.add(model);
		}
		return listContacts;
	}

	public static ArrayList<ContactListSelectableItem> loadDummySelectableContacts() {
		ArrayList<ContactListSelectableItem> dummyContacts = new ArrayList<ContactListSelectableItem>();
		for (int i = 0; i < 20; i++) {
			ContactListSelectableItem selectableContacts = new ContactListSelectableItem();
			ContactModel contact = new ContactModel();
			contact.setName(dummyContact[i]);
			contact.setPhonenumber(dummyPhoneNumbers[i]);
			contact.setUser_id(String.valueOf(i));
			selectableContacts.setContact(contact);
			selectableContacts.setSelected(false);
			dummyContacts.add(selectableContacts);
		}
		Collections.sort(dummyContacts,
				new Comparator<ContactListSelectableItem>() {
					@Override
					public int compare(ContactListSelectableItem lhs,
							ContactListSelectableItem rhs) {
						return (lhs.getContact().getName()
								.compareToIgnoreCase(rhs.getContact().getName()));
					}
				});
		return dummyContacts;
	}

}
