package com.findingfriends.activities;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import app.akexorcist.gdaplibrary.GoogleDirection;
import app.akexorcist.gdaplibrary.GoogleDirection.OnDirectionResponseListener;

import com.actionbarsherlock.app.SherlockActivity;
import com.findingfriends.api.models.GroupOfFriendsResponse;
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

public class GroupOfPeopleInMap extends SherlockActivity {

	public static String GROUP_OF_PEOPLE_INFO = "group_of_people_info";

	private GoogleMap mMap;
	private Location mMyLocation;
	private GPSUtils mGpsUtils;
	private GroupOfFriendsResponse mGroupInfo;
	private GoogleDirection mGd;
	private Document mDoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_group_people_inmap);

		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		String data = getIntent().getStringExtra(GROUP_OF_PEOPLE_INFO);
		mGroupInfo = (GroupOfFriendsResponse) JsonUtil.readJsonString(data,
				GroupOfFriendsResponse.class);
		mGpsUtils = new GPSUtils(this);
		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setCompassEnabled(false);
		mMap.getUiSettings().setRotateGesturesEnabled(false);
		mMap.getUiSettings().setTiltGesturesEnabled(false);
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		mMyLocation = mGpsUtils.getLocationFromProvider();
		LatLng MYLOCATION = new LatLng(mMyLocation.getLatitude(),
				mMyLocation.getLongitude());
		Marker kiel = mMap.addMarker(new MarkerOptions()
				.position(MYLOCATION)
				.title("You")
				.snippet("Your Location")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(mMyLocation.getLatitude(), mMyLocation
						.getLongitude())).zoom(13).bearing(90).tilt(0).build();
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
		pinToMap(mGroupInfo.getGroupOfPeople());

	}

	public void pinToMap(ArrayList<UserWithDistance> users) {
		for (UserWithDistance nearestUser : users) {
			Location f = new Location("Friends");
			f.setLatitude(nearestUser.getUser().getGps_lat());
			f.setLongitude(nearestUser.getUser().getGps_long());
			LatLng FRIEND = new LatLng(nearestUser.getUser().getGps_lat(),
					nearestUser.getUser().getGps_long());
			Marker friends = mMap.addMarker(new MarkerOptions()
					.position(FRIEND)
					.title(nearestUser.getUser().getUserName())
					.snippet(
							nearestUser.getUser().getUserName() + "'s"
									+ "Location")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			if (Network.isConnected(this)) {
				if (Network.whichNetworkIsConnected(this) == Network.WIFI) {

					navigate(f);
				} else {
					navigateUsingMobileData(f);
				}
			}
		}
	}

	private void navigateUsingMobileData(Location mMyFriend) {
		LatLng fromPosition = new LatLng(mMyLocation.getLatitude(),
				mMyLocation.getLongitude());
		LatLng toPosition = new LatLng(mMyFriend.getLatitude(),
				mMyFriend.getLongitude());

		GMapV2Direction md = new GMapV2Direction();

		Document doc = md.getDocument(fromPosition, toPosition,
				GMapV2Direction.MODE_DRIVING);
		if (doc != null) {
			ArrayList<LatLng> directionPoint = md.getDirection(doc);
			PolylineOptions rectLine = new PolylineOptions().width(3).color(
					Color.RED);

			for (int i = 0; i < directionPoint.size(); i++) {
				rectLine.add(directionPoint.get(i));
			}

			mMap.addPolyline(rectLine);
		} else {
			Toast.makeText(GroupOfPeopleInMap.this,
					"Please connect to WIFI for proper navigation.",
					Toast.LENGTH_SHORT).show();

		}
	}

	public void navigate(Location myFriend) {
		final LatLng start = new LatLng(mMyLocation.getLatitude(),
				mMyLocation.getLongitude());
		final LatLng end = new LatLng(myFriend.getLatitude(),
				myFriend.getLongitude());
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 13));

		mGd = new GoogleDirection(this);
		mGd.setLogging(true);
		mGd.request(start, end, GoogleDirection.MODE_DRIVING);
		mGd.setOnDirectionResponseListener(new OnDirectionResponseListener() {
			public void onResponse(String status, Document doc,
					GoogleDirection gd) {
				mDoc = doc;
				mMap.addPolyline(gd.getPolyline(doc, 3, Color.RED));
				mMap.addMarker(new MarkerOptions()
						.position(start)
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

				mMap.addMarker(new MarkerOptions()
						.position(end)
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

			}
		});

	}
}
