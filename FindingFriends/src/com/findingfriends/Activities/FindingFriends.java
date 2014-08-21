package com.findingfriends.activities;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.findingfriends.services.GPSTracker;

public class FindingFriends extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static void startService(Context context) {
		Intent intent = new Intent(context, GPSTracker.class);

		PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);

		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// for 30 mint 60*60*1000
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, 1800000, 60 * 60 * 1000,
				pintent);

	}

}
