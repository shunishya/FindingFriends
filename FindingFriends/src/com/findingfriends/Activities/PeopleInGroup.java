package com.findingfriends.activities;

import java.util.ArrayList;
import java.util.Locale;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.findingfriends.R;
import com.findingfriends.adapter.SearchContactsAdapter;
import com.findingfriends.dbhelpers.ContactDbHelper;
import com.findingfriends.dummyvalue.DummyContacts;
import com.findingfriends.interfaces.ContactAttachDetachListner;
import com.findingfriends.models.ContactModel;
import com.findingfriends.ui.RecipientView;
import com.findingfriends.utils.ContactListSelectableItem;

public class PeopleInGroup extends SherlockActivity implements
		ContactAttachDetachListner, TextWatcher {

	private EditText etSearchContacts;
	private ListView lvSearchContacts;
	private RecipientView mEtWithSelection;
	private SearchContactsAdapter mSearchContactAdapter;
	private ArrayList<ContactListSelectableItem> contactsArray;
	private ArrayList<ContactListSelectableItem> secondaryContactsArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_people_layout);

		mEtWithSelection = (RecipientView) findViewById(R.id.etWithSelections);
		etSearchContacts = (EditText) findViewById(R.id.etSearchContacts);
		contactsArray = new ArrayList<ContactListSelectableItem>();
		secondaryContactsArray = new ArrayList<ContactListSelectableItem>();

		ContactDbHelper contactDbHelper = new ContactDbHelper(this);
		lvSearchContacts = (ListView) findViewById(R.id.lvSearchContacts);
		contactsArray.addAll(DummyContacts.loadDummySelectableContacts());
		secondaryContactsArray.addAll(contactsArray);
		mSearchContactAdapter = new SearchContactsAdapter(this, contactsArray,
				this);
		lvSearchContacts.setAdapter(mSearchContactAdapter);
		mEtWithSelection.setEditText(etSearchContacts);
		mEtWithSelection.setTextWatcher(this);
		mEtWithSelection.addRemovedListner(this);

		lvSearchContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				mSearchContactAdapter.setCheck(contactsArray.get(position));
				mSearchContactAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.people, menu);
		MenuItem item = menu.getItem(0);
		if (mEtWithSelection.getSelectedContact().size() > 0) {
			item.setVisible(true);
		} else {
			item.setVisible(false);
		}

		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * respective screen is shown according to the item selected from the menu
	 * 
	 * @param item
	 *            MenuItem which is selected by the user from menu.
	 * */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.done:
			// TODO direct to map view activity with group of peoples

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		filterSelectedContacts(s.toString());
		mSearchContactAdapter.notifyDataSetChanged();
	}

	@Override
	public void clickedUser(ContactListSelectableItem contact) {
		for (ContactListSelectableItem contact_on_list : contactsArray) {
			if (contact.getContact().getUser_id()
					.equals(contact_on_list.getContact().getUser_id())) {
				contact_on_list.setSelected(contact.isSelected());
				if (contact.isSelected()) {
					mEtWithSelection.addContact(contact.getContact());
				} else {
					mEtWithSelection.removeContact(contact.getContact());
				}
				invalidateOptionsMenu();
				return;
			}
		}

	}

	@Override
	public void contactRemoved(ContactModel mContact) {
		for (ContactListSelectableItem contact_on_list : contactsArray) {
			if (mContact.getUser_id().equals(
					contact_on_list.getContact().getUser_id())) {
				contact_on_list.setSelected(false);
				filterSelectedContacts(mEtWithSelection.getTextOnEditText());
				mEtWithSelection.removeContact(mContact);
				return;
			}
		}
		invalidateOptionsMenu();
	}

	/**
	 * filter the ILoop Contacts according to the text input by the user
	 * 
	 * @param textToFilter
	 *            accepts the input given by the user in
	 *            EditTextWithSelectedItems
	 * 
	 * */
	private void filterSelectedContacts(String textToFilter) {
		contactsArray.clear();
		textToFilter = textToFilter.toLowerCase(Locale.getDefault());
		if (textToFilter.length() == 0) {
			contactsArray.addAll(secondaryContactsArray);
		} else {
			for (ContactListSelectableItem contact : secondaryContactsArray) {
				if (!contact.isSelected()) {
					String contactName = contact.getContact().getName()
							.toLowerCase(Locale.getDefault());
					if (contactName.contains(textToFilter))
						contactsArray.add(contact);
				}
			}
		}
	}

}
