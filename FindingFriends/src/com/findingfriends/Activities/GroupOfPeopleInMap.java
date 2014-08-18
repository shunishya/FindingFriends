package com.findingfriends.activities;

import java.util.ArrayList;

import android.location.Location;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.findingfriends.R;
import com.findingfriends.api.models.GroupOfFriendsResponse;
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

public class GroupOfPeopleInMap extends SherlockActivity {

	public static String GROUP_OF_PEOPLE_INFO = "group_of_people_info";

	private GoogleMap map;
	private Location myLocation;
	private GPSUtils gpsUtils;
	private GroupOfFriendsResponse groupInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_group_people_inmap);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		String data = getIntent().getStringExtra(GROUP_OF_PEOPLE_INFO);
		groupInfo = (GroupOfFriendsResponse) JsonUtil.readJsonString(data,
				GroupOfFriendsResponse.class);
		gpsUtils = new GPSUtils(this);
		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setTiltGesturesEnabled(false);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		myLocation = gpsUtils.getLocation();
		LatLng MYLOCATION = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());
		Marker kiel = map.addMarker(new MarkerOptions()
				.position(MYLOCATION)
				.title("You")
				.snippet("Your Location")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(myLocation.getLatitude(), myLocation
						.getLongitude())).zoom(17).bearing(90).tilt(0).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		pinToMap(groupInfo.getGroupOfPeople());

	}

	public void pinToMap(ArrayList<UserWithDistance> users) {
		for (UserWithDistance nearestUser : users) {
			LatLng FRIEND = new LatLng(nearestUser.getUser().getGps_lat(),
					nearestUser.getUser().getGps_long());
			Marker friends = map.addMarker(new MarkerOptions()
					.position(FRIEND)
					.title(nearestUser.getUser().getUserName())
					.snippet(
							nearestUser.getUser().getUserName() + "'s"
									+ "Location")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		}
	}
}
