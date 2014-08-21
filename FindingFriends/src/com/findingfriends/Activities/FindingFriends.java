package com.findingfriends.activities;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.findingfriends.services.GPSTracker;

public class FindingFriends extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	public static void startService(Context context) {
		boolean alarmUp = (PendingIntent.getBroadcast(context, 0, new Intent(context,
				GPSTracker.class),
				PendingIntent.FLAG_NO_CREATE) != null);

		if (alarmUp) {
			Log.e("myTag", "Alarm is already active");
		} else {
			Log.e("myTag", "Alarm is active");
			Intent intent = new Intent(context, GPSTracker.class);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, cal.get(Calendar.SECOND));

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, 
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
					AlarmManager alarm = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			// for 30 mint 30*60*1000
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
					30*60*1000, pendingIntent);

		}
	}

}
