package com.findingfriends.activities;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import app.akexorcist.gdaplibrary.GoogleDirection;
import app.akexorcist.gdaplibrary.GoogleDirection.OnAnimateListener;
import app.akexorcist.gdaplibrary.GoogleDirection.OnDirectionResponseListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.findingfriends.models.UserWithDistance;
import com.findingfriends.utils.GMapV2Direction;
import com.findingfriends.utils.GPSUtils;
import com.findingfriends.utils.JsonUtil;
import com.findingfriends.utils.Network;
import com.findings.findingfriends.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class NavigateActivity extends SherlockActivity implements
		OnClickListener {

	public static String USER_INFO = "user_info";

	private GoogleMap mMap;
	private Button mBtnWalk, mBtnDrive, mBtnCycle;
	private TextView mTvDistance, mTextProgress;
	private Button mBtnAnimate;
	private GPSUtils mGpsUtils;
	private UserWithDistance mFriend;
	private Location mMyLocation;
	private Location mMyFriend;
	private GoogleDirection mGd;
	private Document mDoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_navigate);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.Navigationmap)).getMap();

		String userInfo = getIntent().getStringExtra(USER_INFO);

		Log.e("User info::>>>>>", userInfo);
		mFriend = (UserWithDistance) JsonUtil.readJsonString(userInfo,
				UserWithDistance.class);

		mBtnWalk = (Button) findViewById(R.id.btnWalk);
		mBtnDrive = (Button) findViewById(R.id.btnDrive);
		mBtnCycle = (Button) findViewById(R.id.btnCycle);
		mBtnAnimate = (Button) findViewById(R.id.btnAnimate);
		mTvDistance = (TextView) findViewById(R.id.tvDistance);
		mTextProgress = (TextView) findViewById(R.id.textProgress);
		mBtnWalk.setOnClickListener(this);
		mBtnCycle.setOnClickListener(this);
		mBtnDrive.setOnClickListener(this);
		mBtnAnimate.setOnClickListener(this);

		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setCompassEnabled(false);
		mMap.getUiSettings().setRotateGesturesEnabled(false);
		mMap.getUiSettings().setTiltGesturesEnabled(false);
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		mGpsUtils = new GPSUtils(this);

		mMyLocation = mGpsUtils.getLocationFromProvider();
		if (mFriend == null) {
			LatLng MYLOCATION = new LatLng(mMyLocation.getLatitude(),
					mMyLocation.getLongitude());
			Marker kiel = mMap
					.addMarker(new MarkerOptions()
							.position(MYLOCATION)
							.title("You")
							.snippet("Your Location")
							.icon(BitmapDescriptorFactory
									.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(mMyLocation.getLatitude(), mMyLocation
							.getLongitude())).zoom(17).bearing(90).tilt(0)
					.build();
			mMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		} else {
			DecimalFormat df = new DecimalFormat("#.##");
			mTvDistance.setText(mTvDistance.getText() + " "
					+ df.format(mFriend.getDist()) + " m");
			mMyFriend = new Location("Friend");
			mMyFriend.setLatitude(mFriend.getUser().getGps_lat());
			mMyFriend.setLongitude(mFriend.getUser().getGps_long());
			mBtnDrive.setTextColor(getResources().getColor(R.color.red));
			if (Network.isConnected(this)) {
				if (Network.whichNetworkIsConnected(this) == Network.WIFI) {
					navigate(GoogleDirection.MODE_DRIVING);
				} else {
					navigateUsingMobileData();
				}
			}
		}
	}

	private void navigateUsingMobileData() {
		LatLng fromPosition = new LatLng(mMyLocation.getLatitude(),
				mMyLocation.getLongitude());
		LatLng toPosition = new LatLng(mMyFriend.getLatitude(),
				mMyFriend.getLongitude());

		GMapV2Direction md = new GMapV2Direction();

		Document doc = md.getDocument(fromPosition, toPosition,
				GMapV2Direction.MODE_DRIVING);
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(3).color(
				Color.RED);

		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}

		mMap.addPolyline(rectLine);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGd.cancelAnimated();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnWalk:
			mMap.clear();
			mBtnWalk.setTextColor(getResources().getColor(R.color.red));
			mBtnDrive.setTextColor(getResources().getColor(
					android.R.color.black));
			mBtnCycle.setTextColor(getResources().getColor(
					android.R.color.black));
			navigate(GoogleDirection.MODE_WALKING);
			break;
		case R.id.btnDrive:
			mMap.clear();
			mBtnWalk.setTextColor(getResources()
					.getColor(android.R.color.black));
			mBtnDrive.setTextColor(getResources().getColor(R.color.red));
			mBtnCycle.setTextColor(getResources().getColor(
					android.R.color.black));
			navigate(GoogleDirection.MODE_DRIVING);
			break;
		case R.id.btnCycle:
			mMap.clear();
			mBtnWalk.setTextColor(getResources()
					.getColor(android.R.color.black));
			mBtnDrive.setTextColor(getResources().getColor(
					android.R.color.black));
			mBtnCycle.setTextColor(getResources().getColor(R.color.red));
			navigate(GoogleDirection.MODE_BICYCLING);
			break;

		case R.id.btnAnimate:
			v.setVisibility(View.GONE);
			mGd.animateDirection(mMap, mGd.getDirection(mDoc),
					GoogleDirection.SPEED_VERY_SLOW, true, false, true, true,
					new MarkerOptions().icon(BitmapDescriptorFactory
							.fromResource(R.drawable.car)), true, false, null);

			break;

		default:
			break;
		}

	}

	public void navigate(String mode) {
		final LatLng start = new LatLng(mMyLocation.getLatitude(),
				mMyLocation.getLongitude());
		final LatLng end = new LatLng(mMyFriend.getLatitude(),
				mMyFriend.getLongitude());
		if (mFriend.getDist() > 1000) {
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 13));
		} else {
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15));
		}

		mGd = new GoogleDirection(this);
		mGd.setLogging(true);
		mGd.request(start, end, mode);
		mGd.setOnDirectionResponseListener(new OnDirectionResponseListener() {
			public void onResponse(String status, Document doc,
					GoogleDirection gd) {
				mDoc = doc;
				mMap.addPolyline(gd.getPolyline(doc, 3, Color.RED));
				mMap.addMarker(new MarkerOptions()
						.position(start)
						.title("You")
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

				mMap.addMarker(new MarkerOptions()
						.position(end)
						.title(mFriend.getUser().getUserName())
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

				mBtnAnimate.setVisibility(View.VISIBLE);
			}
		});

		mGd.setOnAnimateListener(new OnAnimateListener() {
			public void onStart() {
				mTextProgress.setVisibility(View.VISIBLE);
			}

			public void onProgress(int progress, int total) {
				mTextProgress.setText(progress + " / " + total);
			}

			public void onFinish() {
				mBtnAnimate.setVisibility(View.VISIBLE);
				mTextProgress.setVisibility(View.GONE);
			}
		});

	}
}
