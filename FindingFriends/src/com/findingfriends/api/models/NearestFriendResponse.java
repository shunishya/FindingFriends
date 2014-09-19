/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.findingfriends.api.models;

import java.util.ArrayList;

import com.findingfriends.models.UserWithDistance;

/**
 * 
 * @author nitu
 */
public class NearestFriendResponse {

	private boolean error;
	private String message;
	private int count;
	private ArrayList<UserWithDistance> nearestPeople;

	public ArrayList<UserWithDistance> getNearestPeople() {
		return nearestPeople;
	}

	public void setNearestPeople(ArrayList<UserWithDistance> nearestPeople) {
		this.nearestPeople = nearestPeople;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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
