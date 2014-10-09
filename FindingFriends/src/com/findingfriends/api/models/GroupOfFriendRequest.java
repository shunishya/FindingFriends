/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.findingfriends.api.models;

import java.util.List;

/**
 *
 * @author nitu
 */
public class GroupOfFriendRequest {

    private String user_id;
    private List<String> listOfFriends;
    private double my_gps_lat;
    private double my_gps_lng;
    private String device_id;

    public double getGps_lat() {
        return my_gps_lat;
    }

    public void setGps_lat(double gps_lat) {
        this.my_gps_lat = gps_lat;
    }

    public double getGps_long() {
        return my_gps_lng;
    }

    public void setGps_long(double gps_long) {
        this.my_gps_lng = gps_long;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<String> getListOfFriends() {
        return listOfFriends;
    }

    public void setListOfFriends(List<String> listOfFriends) {
        this.listOfFriends = listOfFriends;
    }

	/**
	 * @return the device_id
	 */
	public String getDevice_id() {
		return device_id;
	}

	/**
	 * @param device_id the device_id to set
	 */
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

}
