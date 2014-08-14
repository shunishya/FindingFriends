package com.findingfriends.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class GPSUtils {
	Context mContext;
	Location location;
	Marker kiel;
	private List<Address> addresses;
	private String best;
	String provider;
	LocationManager mlocManager;
	LocationListener mlocListener;
	static LatLng POS_LAT_LNG;
	Criteria criteria;
	private LocationManager locationManager;
	private boolean isGPSEnabled;
	private boolean isNetworkEnabled;
	private double latitude;
	private double longitude;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	public GPSUtils(Context context) {
		this.mContext = context;
		turnGPSOn();
		mlocManager = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		mlocListener = new MyLocationListener();
		criteria = new Criteria();
		best = mlocManager.getBestProvider(criteria, true);
		mlocManager.requestLocationUpdates(best, 0, 0, mlocListener);
		location = mlocManager.getLastKnownLocation(best);

		if (location != null) {
			mlocListener.onLocationChanged(location);

		} else {
			location = getMyLocation();

		}
		// turnGPSOff();
	}

	public void turnGPSOn() {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		mContext.sendBroadcast(intent);

		provider = Settings.Secure.getString(mContext.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (!provider.contains("gps")) {
			// if gps is disabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			mContext.sendBroadcast(poke);
		}
	}

	public void turnGPSOff() {
		String provider = Settings.Secure.getString(
				mContext.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (provider.contains("gps")) { // if gps is enabled
			final Intent poke = new Intent();
			poke.setClassName("com.android.settings",
					"com.android.settings.widget.SettingsAppWidgetProvider");
			poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
			poke.setData(Uri.parse("3"));
			mContext.sendBroadcast(poke);
		}
	}

	/* Class My Location Listener */
	public class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			Geocoder gcd = new Geocoder(mContext.getApplicationContext(),
					Locale.getDefault());
			if (location != null) {
				try {
					addresses = gcd.getFromLocation(location.getLatitude(),
							location.getLongitude(), 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String text = (addresses != null) ? "City : "
						+ addresses.get(0).getSubLocality() + "\n Country : "
						+ addresses.get(0).getCountryName()
						: "Unknown Location";

				String locationValue = "My current location is: " + text;
				POS_LAT_LNG = new LatLng(location.getLatitude(),
						location.getLongitude());

			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(mContext, "Gps Disabled", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(mContext, "Gps Enabled", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			onLocationChanged(mlocManager.getLastKnownLocation(provider));
		}
	}

	public Location getMyLocation() {
		return location;
	}

	public Location getLocationFromProvider() {
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(mContext.LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled // The minimum time between
				// updates in milliseconds
			} else {
				// First get location from Network Provider
				if (isGPSEnabled) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, mlocListener);
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isNetworkEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, mlocListener);
						Log.d("Network", "Network");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}

}
