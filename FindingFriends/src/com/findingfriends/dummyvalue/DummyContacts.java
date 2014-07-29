package com.findingfriends.dummyvalue;

import java.util.ArrayList;

import com.findingfriends.models.ContactModel;

public class DummyContacts {
	private static String[] contactName = { "Amit", "Ram", "Shyam", "Gita", "Sita",
			"Mita", "Lina", "Una", "Hari", "Laxman", "Suman", "Herman" };
	private static String phone = "9841123456";

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

}
