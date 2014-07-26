/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.findingfriends.api.models;

import java.util.List;

import com.findingfriends.models.ContactModel;

/**
 * 
 * @author nitu
 */
public class SyncContactResponse {
	private boolean error;

	private List<ContactModel> appUsers;

	public List<ContactModel> getAppUsers() {
		return appUsers;
	}

	public void setAppUsers(List<ContactModel> appUsers) {
		this.appUsers = appUsers;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
