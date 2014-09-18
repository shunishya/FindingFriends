package com.findingfriends.api.models;

public class RegisterRequest {
	String userName;
	String phoneNumber;
	double gps_lat;
	double gps_long;
	String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public double getGps_lat() {
		return gps_lat;
	}

	public void setGps_lat(double gps_lat) {
		this.gps_lat = gps_lat;
	}

	public double getGps_long() {
		return gps_long;
	}

	public void setGps_long(double gps_long) {
		this.gps_long = gps_long;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
