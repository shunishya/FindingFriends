package com.findingfriends.dbhelpers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.findingfriends.db.model.ContactDb;
import com.findingfriends.models.ContactModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

public class ContactDbHelper {
	private Dao<ContactDb, Integer> mContactsDao;

	public ContactDbHelper(Context mContext) {
		DbHelper dbHelper = (DbHelper) OpenHelperManager.getHelper(mContext,
				DbHelper.class);
		try {
			mContactsDao = dbHelper.getDao(ContactDb.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void theseDataAreDirty(List<ContactDb> contactOnApp) {
		try {
			for (ContactDb imnContact : contactOnApp) {
				imnContact.setDeleted(true);
				mContactsDao.update(imnContact);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<ContactDb> getDirtyData() {
		try {
			return mContactsDao.queryForEq(ContactDb.FIELD_DELETED, true);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int notifyUpdated() {
		int updateCount = 0;
		for (ContactDb imnContact : getUnUpdatedContacts()) {
			imnContact.setUpdated(true);
			try {
				mContactsDao.update(imnContact);
				updateCount++;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return updateCount;
	}

	public List<ContactDb> getContactsOnApp() {
		List<ContactDb> contactsOnApp = null;
		try {
			contactsOnApp = mContactsDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return contactsOnApp;
	}

	public List<ContactModel> getNonAppUsers() {
		List<ContactModel> nonIloopContacts = new ArrayList<ContactModel>();
		try {
			for (ContactDb contact : mContactsDao.queryBuilder().where()
					.isNull(ContactDb.FIELD_USER_ID).query()) {
				ContactModel model = new ContactModel();
				model.setPhonenumber(contact.getPhone());
				nonIloopContacts.add(model);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return nonIloopContacts;
	}

	public List<String> getDeletedContactUId() {
		List<String> deletedContacts = new ArrayList<String>();
		try {
			for (ContactDb contact : mContactsDao.queryBuilder().where()
					.isNotNull(ContactDb.FIELD_USER_ID).and()
					.eq(ContactDb.FIELD_DELETED, true).query()) {
				deletedContacts.add(contact.getUserId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return deletedContacts;
	}

	public void updateContacts(List<ContactDb> contactsToUpdate) {
		for (ContactDb imnContact : contactsToUpdate) {
			updateContact(imnContact);
		}
	}

	public void updateContact(ContactDb contactToUpdate) {
		try {
			mContactsDao.update(contactToUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void newUsers(List<ContactModel> contacts) {
		for (ContactModel resultContacts : contacts) {
			try {
				ContactDb contact = mContactsDao.queryBuilder().where()
						.eq(ContactDb.FIELD_PHONE, resultContacts.getPhonenumber())
						.queryForFirst();
				if (contact != null) {
					contact.setUserId(resultContacts.getUser_id());
					mContactsDao.update(contact);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public int updateDbFromWebService(List<ContactModel> contacts) {
		int updateCount = 0;
		try {
			List<ContactDb> appContacts = getContactsOnApp();
			for (int i = 0; i < contacts.size(); i++) {
				for (int j = 0; j < appContacts.size(); j++) {
					if (contacts.get(i).getPhonenumber()
							.equals(appContacts.get(j).getPhone())) {
						ContactModel response = contacts.get(i);
						ContactDb contactToUpdate = appContacts.get(j);
						contactToUpdate.setUserId(response.getUser_id());
						mContactsDao.update(contactToUpdate);
						updateCount++;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updateCount;
	}

	public void initDbWithItems(List<ContactDb> contactsToInsert) {
		try {
			for (ContactDb imnContact : contactsToInsert) {
				mContactsDao.create(imnContact);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void theseContactsAreNotSynced(List<ContactDb> unSyncedContact) {
		try {
			for (ContactDb imnContact : unSyncedContact) {
				imnContact.setUpdated(false);
				mContactsDao.createOrUpdate(imnContact);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addNewContacts(List<ContactDb> newContact) {
		for (ContactDb imnContact : newContact) {
			createContact(imnContact);
		}
	}

	public void createContact(ContactDb contact) {
		contact.setUpdated(false);
		try {
			mContactsDao.create(contact);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<ContactDb> getUnUpdatedContacts() {
		try {
			return mContactsDao.queryBuilder().where()
					.isNotNull(ContactDb.FIELD_USER_ID).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isTableEmpty() {
		try {
			if (mContactsDao.countOf() == 0)
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int deleteDirtyData() {
		try {
			List<ContactDb> dirtyContacts = mContactsDao.queryForEq(
					ContactDb.FIELD_DELETED, true);
			mContactsDao.delete(dirtyContacts);
			return dirtyContacts.size();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void deleteDirtyData(List<ContactDb> dirtyContacts) {
		try {
			mContactsDao.delete(dirtyContacts);

			// return dirtyContacts.size();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// return 0;
	}

	public void deleteContact(ContactDb contact) {
		try {
			mContactsDao.delete(contact);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setDataUpdated(List<ContactDb> contactsSynced) {
		try {
			for (ContactDb imnContact : contactsSynced) {
				imnContact.setUpdated(true);
				mContactsDao.update(imnContact);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*public List<ContactListSelectableItem> getSelectableIloopContacts() {
		try {
			List<ContactListSelectableItem> selectableContacts = new ArrayList<ContactListSelectableItem>();
			List<ContactDb> mContactsOnImn = mContactsDao.queryBuilder()
					.orderBy(ContactDb.FIELD_NAME, true).where()
					.isNotNull(ContactDb.FIELD_USER_ID).query();
			for (ContactDb contact : mContactsOnImn) {
				ContactListSelectableItem mSelectableContact = new ContactListSelectableItem();
				mSelectableContact.setContact(contact);
				selectableContacts.add(mSelectableContact);
			}
			return selectableContacts;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/

	public List<ContactDb> getCleanContacts(boolean orderByName) {
		try {
			if (orderByName)
				return mContactsDao.queryBuilder().orderBy("name", true)
						.where().eq(ContactDb.FIELD_DELETED, false).query();
			else
				return mContactsDao.queryForEq(ContactDb.FIELD_DELETED, false);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<ContactDb> getAppUsersOnly() {
		try {
			return mContactsDao.queryBuilder()
					.orderBy(ContactDb.FIELD_NAME, true).where()
					.isNotNull(ContactDb.FIELD_USER_ID).and()
					.eq(ContactDb.FIELD_DELETED, false).query();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public ContactDb getContactFromId(String contact_Id) {
		try {
			return mContactsDao.queryBuilder().where()
					.eq(ContactDb.FIELD_USER_ID, contact_Id).queryForFirst();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getAllContactId() {
		List<String> userIds = new ArrayList<String>();
		try {
			List<ContactDb> contacts = mContactsDao.queryBuilder().distinct()
					.selectColumns(ContactDb.FIELD_USER_ID).where()
					.isNotNull(ContactDb.FIELD_USER_ID).query();
			for (ContactDb contact : contacts) {
				userIds.add(contact.getUserId());
			}
			return userIds;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

}
