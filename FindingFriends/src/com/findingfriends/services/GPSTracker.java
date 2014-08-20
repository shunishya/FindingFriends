package com.findingfriends.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.findingfriends.utils.GPSUtils;

public class GPSTracker extends Service {

	protected LocationManager locationManager;
	private Context mContext;
	private GPSUtils gpsUtils;
	private Location location;
	private Handler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler(Looper.getMainLooper());
		gpsUtils = new GPSUtils(getApplicationContext());
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Log.e("GPS Service:>>>", "runnable called.");
				if (gpsUtils.isProviderEnable()) {
					Log.e("GPS Service:>>>", "Provider called.");
					location = gpsUtils.getLocationFromProvider();
					Log.e("GPS Service:>>>", location + "");
				} else {
					gpsUtils.showSettingsAlert("GPS");
					if (gpsUtils.isProviderEnable()) {
						Log.e("GPS Service:>>>", "Provider1 called.");
						location = gpsUtils.getLocationFromProvider();
					}
				}
				handler.post(this);

			}
		}, 1800000);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
