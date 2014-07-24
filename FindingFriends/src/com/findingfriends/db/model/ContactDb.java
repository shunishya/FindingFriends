package com.findingfriends.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = DbConstants.TABLE_CONTACT_LIST)
public class ContactDb {
	public static final String FIELD_NAME = "name";
	public static final String FIELD_DELETED = "deleted";
	public static final String FIELD_UPDATED = "updated";
	public static final String FIELD_ID = "id";
	public static final String FIELD_USER_ID = "userId";
	public static final String FIELD_PHONE = "phone";

	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String name;
	@DatabaseField
	private String phone;
	@DatabaseField
	private String userId;
	@DatabaseField
	private boolean updated;
	@DatabaseField
	private boolean deleted;

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserId() {
		return userId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
