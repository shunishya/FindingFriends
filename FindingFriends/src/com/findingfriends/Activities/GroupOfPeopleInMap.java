package com.findingfriends.activities;

import java.util.ArrayList;

import org.w3c.dom.Document;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import app.akexorcist.gdaplibrary.GoogleDirection;
import app.akexorcist.gdaplibrary.GoogleDirection.OnAnimateListener;
import app.akexorcist.gdaplibrary.GoogleDirection.OnDirectionResponseListener;

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
	private GoogleDirection gd;
	private Document mDoc;

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

		myLocation = gpsUtils.getLocationFromProvider();
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
			Location f = new Location("Friends");
			f.setLatitude(nearestUser.getUser().getGps_lat());
			f.setLongitude(nearestUser.getUser().getGps_long());
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
			navigate(f);
		}
	}

	public void navigate(Location myFriend) {
		final LatLng start = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());
		final LatLng end = new LatLng(myFriend.getLatitude(),
				myFriend.getLongitude());

		map.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15));

		gd = new GoogleDirection(this);
		gd.setLogging(true);
		gd.request(start, end, GoogleDirection.MODE_DRIVING);
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

			}
		});

	}
}
