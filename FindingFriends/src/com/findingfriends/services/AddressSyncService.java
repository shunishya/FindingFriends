package com.findingfriends.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.findingfriends.api.FindingFriendsApi;
import com.findingfriends.api.FindingFriendsException;
import com.findingfriends.api.models.ContactSyncRequest;
import com.findingfriends.api.models.SyncContactResponse;
import com.findingfriends.db.model.ContactDb;
import com.findingfriends.dbhelpers.ContactDbHelper;
import com.findingfriends.helpers.PhoneNumberHelper;
import com.findingfriends.models.ContactModel;
import com.findingfriends.utils.DeviceUtils;
import com.findingfriends.utils.FindingFriendsPreferences;

public class AddressSyncService extends Service {
	private ContactDbHelper mDbDigger;
	private String defaultCountryCode;

	@Override
	public void onCreate() {
		super.onCreate();
		if (defaultCountryCode == null)
			defaultCountryCode = DeviceUtils
					.getCountryIso(getApplicationContext());
		mDbDigger = new ContactDbHelper(getApplicationContext());
		updateDb();
		new ContactSyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

	}

	private void updateDb() {
		List<ContactDb> phoneContacts = getPhoneBookContacts();
		if (phoneContacts.isEmpty()) {
			return;
		}
		if (mDbDigger.isTableEmpty()) {
			mDbDigger.initDbWithItems(phoneContacts);
		} else {
			compareContacts(phoneContacts, mDbDigger.getContactsOnApp());
		}
	}

	/**
	 * Get Contacts from phone book here
	 */
	public List<ContactDb> getPhoneBookContacts() {
		List<ContactDb> phoneContacts = new ArrayList<ContactDb>();
		PhoneNumberHelper phoneHepler = new PhoneNumberHelper();

		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (phones.moveToNext()) {
			String name = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			phoneNumber = phoneNumber.replaceAll("-", "");
			phoneNumber = phoneNumber.replaceAll(" ", "");
			ContactModel contact = phoneHepler.getContactNumberCountry(
					phoneNumber, defaultCountryCode);
			if (contact != null) {
				ContactDb contactDb = new ContactDb();
				contactDb.setPhone(contact.getPhonenumber());
				contactDb.setName(name);
				phoneContacts.add(contactDb);
			}
		}
		phones.close();
		return phoneContacts;
	}

	private void compareContacts(List<ContactDb> contactsOnDevice,
			List<ContactDb> contactsOnApp) {
		HashMap<String, ContactDb> contactHashMapOnDevice = new HashMap<String, ContactDb>();
		HashMap<String, ContactDb> contactHashMapOnPhone = new HashMap<String, ContactDb>();
		HashMap<String, ContactDb> addedContacats = new HashMap<String, ContactDb>();
		List<ContactDb> contactsDeleted = new ArrayList<ContactDb>();
		List<ContactDb> contactsNameChanged = new ArrayList<ContactDb>();
		for (ContactDb contact : contactsOnApp) {
			contactHashMapOnDevice.put(contact.getPhone(), contact);
		}
		for (ContactDb contact : contactsOnDevice) {
			contactHashMapOnPhone.put(contact.getPhone(), contact);
		}
		addedContacats.putAll(contactHashMapOnPhone);
		Iterator<Map.Entry<String, ContactDb>> it = contactHashMapOnDevice
				.entrySet().iterator();
		while (it.hasNext()) {
			ContactDb appContact = it.next().getValue();
			if (contactHashMapOnPhone.containsKey(appContact.getPhone())) {
				ContactDb phoneContact = contactHashMapOnPhone.get(appContact
						.getPhone());
				if (!phoneContact.getName().equals(appContact.getName())) {
					appContact.setName(phoneContact.getName());
					contactsNameChanged.add(appContact);
				}
				addedContacats.remove(appContact.getPhone());
			} else {
				contactsDeleted.add(appContact);
			}
		}
		mDbDigger.updateContacts(contactsNameChanged);
		Iterator<Map.Entry<String, ContactDb>> addedContactsIterator = addedContacats
				.entrySet().iterator();
		while (addedContactsIterator.hasNext()) {
			mDbDigger.createContact(addedContactsIterator.next().getValue());
		}
		for (ContactDb contact : contactsDeleted) {
			if (contact.getUserId() != null) {
				contact.setDeleted(true);
				mDbDigger.updateContact(contact);
			} else {
				mDbDigger.deleteContact(contact);
			}
		}
	}

	/**
	 * Asynctask for contact sync
	 * 
	 * 
	 */
	private class ContactSyncTask extends AsyncTask<Void, Void, Object> {
		FindingFriendsApi api = new FindingFriendsApi(getApplicationContext());
		FindingFriendsPreferences mPrefs = new FindingFriendsPreferences(
				getApplicationContext());

		@Override
		protected Object doInBackground(Void... params) {
			ContactSyncRequest syncRequest = new ContactSyncRequest();
			List<ContactModel> contactsToBeAdd = mDbDigger.getNonAppUsers();
			List<String> contactsTobeDelete = mDbDigger.getDeletedContactUId();
			syncRequest.setContactsTobeAdd(contactsToBeAdd);
			syncRequest.setContactsToBeDeleted(contactsTobeDelete);
			syncRequest.setUser_id(mPrefs.getUserID());
			try {
				return api.contactSync(syncRequest);
			} catch (FindingFriendsException e) {
				e.printStackTrace();
				return e;
			}

		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result instanceof SyncContactResponse) {
				SyncContactResponse res = (SyncContactResponse) result;
				if (res.isError()) {
					Toast.makeText(getApplicationContext(), res.getMessage(),
							Toast.LENGTH_SHORT).show();
				} else {
					int count = mDbDigger.updateDbFromWebService(res
							.getAppUsers());
					Toast.makeText(getApplicationContext(),
							"Updates Contacts: " + count, Toast.LENGTH_SHORT)
							.show();
				}
			} else if (result instanceof FindingFriendsException) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_SHORT).show();
			}
			stopSelf();

		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
