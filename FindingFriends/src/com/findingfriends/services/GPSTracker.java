package com.findingfriends.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.findingfriends.api.FindingFriendsApi;
import com.findingfriends.api.FindingFriendsException;
import com.findingfriends.api.models.UpdateLocation;
import com.findingfriends.api.models.UpdateLocationResponse;
import com.findingfriends.utils.FindingFriendsPreferences;
import com.findingfriends.utils.GPSUtils;

public class GPSTracker extends Service {

	protected LocationManager locationManager;
	private GPSUtils gpsUtils;
	private Location location;
	private Handler handler;
	private FindingFriendsPreferences mPrefs;

	@Override
	public void onCreate() {
		super.onCreate();
		handler = new Handler(Looper.getMainLooper());
		gpsUtils = new GPSUtils(getApplicationContext());
		mPrefs = new FindingFriendsPreferences(getApplicationContext());
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Log.e("GPS Service:>>>", "runnable called.");
				UpdateLocation request = new UpdateLocation();
				request.setUser_id(mPrefs.getUserID());
				if (gpsUtils.isProviderEnable()) {
					Log.e("GPS Service:>>>", "Provider called.");
					location = gpsUtils.getLocationFromProvider();
					Log.e("GPS Service:>>>", location + "");
					request.setGps_lat(location.getLatitude());
					request.setGps_long(location.getLongitude());
					new UpdateMyLocation().execute(request);
				} else {
					gpsUtils.showSettingsAlert("GPS");
					if (gpsUtils.isProviderEnable()) {
						Log.e("GPS Service:>>>", "Provider1 called.");
						location = gpsUtils.getLocationFromProvider();
						request.setGps_lat(location.getLatitude());
						request.setGps_long(location.getLongitude());
						new UpdateMyLocation().execute(request);
					}
				}
				handler.post(this);

			}
		}, 1800000);

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public class UpdateMyLocation extends
			AsyncTask<UpdateLocation, Void, Object> {
		FindingFriendsApi api = new FindingFriendsApi(getApplicationContext());

		@Override
		protected Object doInBackground(UpdateLocation... params) {
			try {
				return api.updateLocation(params[0]);
			} catch (FindingFriendsException e) {
				e.printStackTrace();
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result instanceof UpdateLocationResponse) {
				UpdateLocationResponse res = (UpdateLocationResponse) result;
				if (res.isError()) {
					Toast.makeText(getApplicationContext(),
							"Location not updated..", Toast.LENGTH_SHORT)
							.show();
				}

			} else if (result instanceof FindingFriendsException) {
				FindingFriendsException error = (FindingFriendsException) result;
				Toast.makeText(getApplicationContext(), error.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}

	}
}
