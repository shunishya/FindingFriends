/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.findingfriends.api.models;

import com.findingfriends.models.ContactModel;

import java.util.List;

/**
 * 
 * @author nitu
 */
public class SyncContactResponse {
	private boolean error;

	private List<ContactModel> appUsers;

	private String message;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
