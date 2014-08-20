package com.findingfriends.activities;

import com.findingfriends.services.GPSTracker;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class FindingFriends extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static void startService(Context context) {
		Log.e("GPS Service:>>>", "startService called.");
		Intent gpsIntent = new Intent(context, GPSTracker.class);
		context.startService(gpsIntent);
	}

}
