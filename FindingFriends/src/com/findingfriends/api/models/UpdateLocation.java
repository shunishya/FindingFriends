package com.findingfriends.api.models;

public class UpdateLocation {
	private String user_id;
	private double gps_lat;
	private double gps_long;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
}
