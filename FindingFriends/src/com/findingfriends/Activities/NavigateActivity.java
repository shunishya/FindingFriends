package com.findingfriends.activities;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import app.akexorcist.gdaplibrary.GoogleDirection;
import app.akexorcist.gdaplibrary.GoogleDirection.OnAnimateListener;
import app.akexorcist.gdaplibrary.GoogleDirection.OnDirectionResponseListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.findingfriends.R;
import com.findingfriends.models.UserWithDistance;
import com.findingfriends.utils.GPSUtils;
import com.findingfriends.utils.JsonUtil;
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

	public static String USER_INFO = "user_info";

	private GoogleMap map;
	private Button btnWalk, btnDrive, btnCycle;
	private TextView tvDistance, textProgress;
	private Button btnAnimate;
	private GPSUtils gpsUtils;
	private UserWithDistance friend;
	private Location myLocation;
	private Location myFriend;
	private GoogleDirection gd;
	private Document mDoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_navigate);
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.Navigationmap)).getMap();

		String userInfo = getIntent().getStringExtra(USER_INFO);
		friend = (UserWithDistance) JsonUtil.readJsonString(userInfo,
				UserWithDistance.class);

		btnWalk = (Button) findViewById(R.id.btnWalk);
		btnDrive = (Button) findViewById(R.id.btnDrive);
		btnCycle = (Button) findViewById(R.id.btnCycle);
		btnAnimate = (Button) findViewById(R.id.btnAnimate);
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		textProgress = (TextView) findViewById(R.id.textProgress);
		btnWalk.setOnClickListener(this);
		btnCycle.setOnClickListener(this);
		btnDrive.setOnClickListener(this);
		btnAnimate.setOnClickListener(this);

		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setTiltGesturesEnabled(false);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		gpsUtils = new GPSUtils(this);

		myLocation = gpsUtils.getLocation();
		if (friend == null) {
			LatLng MYLOCATION = new LatLng(myLocation.getLatitude(),
					myLocation.getLongitude());
			Marker kiel = map
					.addMarker(new MarkerOptions()
							.position(MYLOCATION)
							.title("You")
							.snippet("Your Location")
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(myLocation.getLatitude(), myLocation
							.getLongitude())).zoom(17).bearing(90).tilt(0)
					.build();
			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		} else {
			myFriend = new Location("Friend");
			myFriend.setLatitude(friend.getUser().getGps_lat());
			myFriend.setLongitude(friend.getUser().getGps_long());
			navigate(GoogleDirection.MODE_DRIVING);
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		gd.cancelAnimated();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWalk:
			map.clear();
			navigate(GoogleDirection.MODE_WALKING);
			break;
		case R.id.btnDrive:
			map.clear();
			navigate(GoogleDirection.MODE_DRIVING);
			break;
		case R.id.btnCycle:
			map.clear();
			navigate(GoogleDirection.MODE_BICYCLING);
			break;

		case R.id.btnAnimate:
			v.setVisibility(View.GONE);
			gd.animateDirection(map, gd.getDirection(mDoc),
					GoogleDirection.SPEED_VERY_SLOW, true, false, true, true,
					new MarkerOptions().icon(BitmapDescriptorFactory
							.fromResource(R.drawable.car)), true, false, null);

			break;

		default:
			break;
		}

	}

	public void navigate(String mode) {
		final LatLng start = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());
		final LatLng end = new LatLng(myFriend.getLatitude(),
				myFriend.getLongitude());

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15));

		gd = new GoogleDirection(this);
		gd.setLogging(true);
		gd.request(start, end, mode);
		gd.setOnDirectionResponseListener(new OnDirectionResponseListener() {
			public void onResponse(String status, Document doc,
					GoogleDirection gd) {
				mDoc = doc;
				map.addPolyline(gd.getPolyline(doc, 3, Color.RED));
				map.addMarker(new MarkerOptions()
						.position(start)
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

				map.addMarker(new MarkerOptions()
						.position(end)
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

				btnAnimate.setVisibility(View.VISIBLE);
			}
		});

		gd.setOnAnimateListener(new OnAnimateListener() {
			public void onStart() {
				textProgress.setVisibility(View.VISIBLE);
			}

			public void onProgress(int progress, int total) {
				textProgress.setText(progress + " / " + total);
			}

			public void onFinish() {
				btnAnimate.setVisibility(View.VISIBLE);
				textProgress.setVisibility(View.GONE);
			}
		});

	}
}
