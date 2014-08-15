package com.findingfriends.activities;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.findingfriends.R;
import com.findingfriends.utils.GPSUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NavigateActivity extends SherlockActivity implements
		OnClickListener {

	private GoogleMap map;
	private Button btnWalk, btnDrive, btnCycle;
	private GPSUtils gpsUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_navigate);
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.Navigationmap)).getMap();
		btnWalk = (Button) findViewById(R.id.btnWalk);
		btnDrive = (Button) findViewById(R.id.btnDrive);
		btnCycle = (Button) findViewById(R.id.btnCycle);
		btnWalk.setOnClickListener(this);
		btnCycle.setOnClickListener(this);
		btnDrive.setOnClickListener(this);

		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setTiltGesturesEnabled(false);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		gpsUtils = new GPSUtils(this);

		Location location = gpsUtils.getLocationFromProvider();

		LatLng KIEL = new LatLng(location.getLatitude(),
				location.getLongitude());
		Marker kiel = map.addMarker(new MarkerOptions()
				.position(KIEL)
				.title("You")
				.snippet("Your Location")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(location.getLatitude(), location
						.getLongitude())).zoom(17).bearing(90).tilt(0).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWalk:

			break;
		case R.id.btnDrive:

			break;
		case R.id.btnCycle:

			break;

		default:
			break;
		}

	}
}
