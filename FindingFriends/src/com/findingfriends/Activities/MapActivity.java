package com.findingfriends.activities;

import im.dino.dbinspector.activities.DbInspectorActivity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.findingfriends.R;
import com.findingfriends.adapter.NearestPeopleAdapter;
import com.findingfriends.api.FindingFriendsApi;
import com.findingfriends.api.FindingFriendsException;
import com.findingfriends.api.models.NearestFriendRequest;
import com.findingfriends.api.models.NearestFriendResponse;
import com.findingfriends.interfaces.AdapterToActivity;
import com.findingfriends.models.UserWithDistance;
import com.findingfriends.utils.FindingFriendsPreferences;
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

public class MapActivity extends SherlockActivity implements OnClickListener,
		AdapterToActivity {
	private GoogleMap map;
	private ListView lvNearestPeople;
	private Button btnAway;
	private Button btnFullMap;
	private NearestPeopleAdapter mAdapter;

	private GPSUtils gpsUtils;
	private TextView tvInfo;
	private NearestFriendRequest request;
	private FindingFriendsPreferences mPrefs;
	public MenuItem refreshItem = null;
	private Location myLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_map_activity);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		lvNearestPeople = (ListView) findViewById(R.id.lvNearestPeople);
		tvInfo = (TextView) findViewById(R.id.tvInfo);
		btnAway = (Button) findViewById(R.id.btnAway);
		btnFullMap = (Button) findViewById(R.id.btnViewFullMap);
		btnAway.setOnClickListener(this);
		btnFullMap.setOnClickListener(this);
		mPrefs = new FindingFriendsPreferences(this);

		gpsUtils = new GPSUtils(this);

		myLocation = gpsUtils.getLocationFromProvider();

		map.setMyLocationEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setCompassEnabled(false);
		map.getUiSettings().setRotateGesturesEnabled(false);
		map.getUiSettings().setTiltGesturesEnabled(false);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		if (myLocation != null) {
			pointMylocation();
		} else {
			new GetMyLocation()
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}

	}

	public void pointMylocation() {
		LatLng KIEL = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());
		Marker kiel = map.addMarker(new MarkerOptions()
				.position(KIEL)
				.title("You")
				.snippet("Your Location")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(myLocation.getLatitude(), myLocation
						.getLongitude())).zoom(17).bearing(90).tilt(0).build();
		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

		gpsUtils.turnGPSOff();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnViewFullMap:
			if (tvInfo.getVisibility() == View.VISIBLE) {
				tvInfo.setVisibility(View.GONE);
				lvNearestPeople.setVisibility(View.GONE);
				btnFullMap.setText("View List");
			} else {
				tvInfo.setVisibility(View.VISIBLE);
				lvNearestPeople.setVisibility(View.VISIBLE);
				btnFullMap.setText("Full map");
			}
			break;
		case R.id.btnAway:
			Intent findGroupOfPeopleIntent = new Intent(MapActivity.this,
					PeopleInGroup.class);
			findGroupOfPeopleIntent.putExtra(PeopleInGroup.LATITUDE,
					myLocation.getLatitude());
			findGroupOfPeopleIntent.putExtra(PeopleInGroup.LONGITUDE,
					myLocation.getLongitude());

			startActivity(findGroupOfPeopleIntent);
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
		menu.add(Menu.NONE, 1, Menu.NONE, "Refresh data")
				.setIcon(R.drawable.navigation_refresh)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
		setRefreshItem(menu.findItem(1));
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * respective screen is shown according to the item selected from the menu
	 * 
	 * @param item
	 *            MenuItem which is selected by the user from menu.
	 * */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.showDb:
			startActivity(new Intent(MapActivity.this,
					DbInspectorActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
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

	protected void setRefreshItem(MenuItem item) {
		refreshItem = item;

		if (myLocation != null) {
			request = new NearestFriendRequest();
			request.setLat(myLocation.getLatitude());
			request.setLog(myLocation.getLongitude());
			request.setUser_id(mPrefs.getUserID());
			new GetNearestFriends().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, request);
		}
	}

	protected void stopRefresh() {
		if (refreshItem != null) {
			refreshItem.setActionView(null);
		}
	}

	protected void runRefresh() {
		if (refreshItem != null) {
			refreshItem
					.setActionView(R.layout.actionbar_indeterminate_progress);
		}
	}

	public class GetNearestFriends extends
			AsyncTask<NearestFriendRequest, Void, Object> {
		FindingFriendsApi api = new FindingFriendsApi(MapActivity.this);

		@Override
		protected Object doInBackground(NearestFriendRequest... params) {
			try {
				return api.getNearestFriends(params[0]);
			} catch (FindingFriendsException e) {
				e.printStackTrace();
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			stopRefresh();
			if (result instanceof NearestFriendResponse) {
				NearestFriendResponse response = (NearestFriendResponse) result;
				if (!response.isError()) {
					pinToMap(response.getNearestPeople());
					mAdapter = new NearestPeopleAdapter(MapActivity.this,
							response.getNearestPeople(), MapActivity.this);
					lvNearestPeople.setAdapter(mAdapter);
				} else {
					Toast.makeText(MapActivity.this, response.getMessage(),
							Toast.LENGTH_SHORT).show();
				}
			} else if (result instanceof FindingFriendsException) {
				FindingFriendsException error = (FindingFriendsException) result;
				Toast.makeText(MapActivity.this, error.toString(),
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			runRefresh();
		}
	}

	@Override
	public void navigate(UserWithDistance user) {
		Intent intent = new Intent(this, NavigateActivity.class);
		intent.putExtra(NavigateActivity.USER_INFO, JsonUtil.writeValue(user));
		startActivity(intent);

	}

	@Override
	public void makeACall(String phoneNumber) {
		Intent callIntent = new Intent(Intent.ACTION_DIAL);
		callIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber)));
		callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(callIntent);
	}

	@SuppressLint("NewApi")
	@Override
	public void sendMsg(String phoneNumber) {
		Intent intent;
		String text = "Im here";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // Android 4.4
																	// and up
		{
			String defaultSmsPackageName = Telephony.Sms
					.getDefaultSmsPackage(this);

			intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
					+ Uri.encode(phoneNumber)));
			intent.putExtra("sms_body", text);

			if (defaultSmsPackageName != null) // Can be null in case that there
												// is no default, then the user
												// would be able to choose any
												// app that supports this
												// intent.
			{
				intent.setPackage(defaultSmsPackageName);
			}
			startActivity(intent);
		} else {
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setType("vnd.android-dir/mms-sms");
			intent.putExtra("address", phoneNumber);
			intent.putExtra("sms_body", "Im here.");
			startActivity(intent);
		}

	}

	public class GetMyLocation extends AsyncTask<Void, Void, Object> {

		@Override
		protected Object doInBackground(Void... arg0) {

			return gpsUtils.getLocationFromProvider();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result instanceof Location) {
				myLocation = (Location) result;
				if (myLocation == null) {
					new GetMyLocation()
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					pointMylocation();
					request = new NearestFriendRequest();
					request.setLat(myLocation.getLatitude());
					request.setLog(myLocation.getLongitude());
					request.setUser_id(mPrefs.getUserID());
					new GetNearestFriends().executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, request);
				}
			}
		}

	}
}
