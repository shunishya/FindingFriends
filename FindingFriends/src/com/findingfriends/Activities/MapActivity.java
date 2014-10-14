package com.findingfriends.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;
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
import com.findingfriends.adapter.NearestPeopleAdapter;
import com.findingfriends.api.FindingFriendsApi;
import com.findingfriends.api.FindingFriendsException;
import com.findingfriends.api.models.NearestFriendRequest;
import com.findingfriends.api.models.NearestFriendResponse;
import com.findingfriends.interfaces.AdapterToActivity;
import com.findingfriends.models.UserWithDistance;
import com.findingfriends.services.AddressSyncService;
import com.findingfriends.services.AddressSyncService.LocalBinder;
import com.findingfriends.utils.DeviceUtils;
import com.findingfriends.utils.FindingFriendsPreferences;
import com.findingfriends.utils.GPSUtils;
import com.findingfriends.utils.JsonUtil;
import com.findings.findingfriends.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapActivity extends SherlockActivity implements OnClickListener,
		AdapterToActivity {
	private GoogleMap mMap;
	private ListView mLvNearestPeople;
	private Button mBtnAway;
	private Button mBtnFullMap;
	private NearestPeopleAdapter mAdapter;

	private GPSUtils mGpsUtils;
	private TextView mTvInfo;
	private NearestFriendRequest mRequest;
	private FindingFriendsPreferences mPrefs;
	public MenuItem mRefreshItem = null;
	private Location mMyLocation;

	private boolean mBounded;
	private AddressSyncService mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_map_activity);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mLvNearestPeople = (ListView) findViewById(R.id.lvNearestPeople);
		mTvInfo = (TextView) findViewById(R.id.tvInfo);
		mBtnAway = (Button) findViewById(R.id.btnAway);
		mBtnFullMap = (Button) findViewById(R.id.btnViewFullMap);
		mBtnAway.setOnClickListener(this);
		mBtnFullMap.setOnClickListener(this);
		mPrefs = new FindingFriendsPreferences(this);

		mGpsUtils = new GPSUtils(this);

		mMyLocation = mGpsUtils.getLocationFromProvider();

		mMap.setMyLocationEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setCompassEnabled(false);
		mMap.getUiSettings().setRotateGesturesEnabled(false);
		mMap.getUiSettings().setTiltGesturesEnabled(false);
		mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		if (mMyLocation != null) {
			pointMylocation();
		} else {
			new GetMyLocation()
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}

	}

	public void pointMylocation() {
		LatLng KIEL = new LatLng(mMyLocation.getLatitude(),
				mMyLocation.getLongitude());
		Marker kiel = mMap.addMarker(new MarkerOptions()
				.position(KIEL)
				.title("You")
				.snippet("Your Location")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(mMyLocation.getLatitude(), mMyLocation
						.getLongitude())).zoom(13).bearing(90).tilt(0).build();
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));

		// mGpsUtils.turnGPSOff();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mBounded) {
			if (mService != null)
				mService.setMapActivity(null);
			unbindService(mConnection);
			mBounded = false;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnViewFullMap:
			if (mTvInfo.getVisibility() == View.VISIBLE) {
				mTvInfo.setVisibility(View.GONE);
				mLvNearestPeople.setVisibility(View.GONE);
				mBtnFullMap.setText("View List");
			} else {
				mTvInfo.setVisibility(View.VISIBLE);
				mLvNearestPeople.setVisibility(View.VISIBLE);
				mBtnFullMap.setText("Full map");
			}
			break;
		case R.id.btnAway:
			Intent findGroupOfPeopleIntent = new Intent(MapActivity.this,
					PeopleInGroup.class);
			findGroupOfPeopleIntent.putExtra(PeopleInGroup.LATITUDE,
					mMyLocation.getLatitude());
			findGroupOfPeopleIntent.putExtra(PeopleInGroup.LONGITUDE,
					mMyLocation.getLongitude());

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
		case R.id.refresh:
			setRefreshItem(item);
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
			Marker friends = mMap.addMarker(new MarkerOptions()
					.position(FRIEND)
					.title(nearestUser.getUser().getUserName())
					.snippet(
							nearestUser.getUser().getUserName() + "'s"
									+ "Location")
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
		}
	}

	public void getFriends() {
		invalidateOptionsMenu();
	}

	protected void setRefreshItem(MenuItem item) {
		mRefreshItem = item;

		if (mMyLocation != null) {
			mRequest = new NearestFriendRequest();
			mRequest.setLat(mMyLocation.getLatitude());
			mRequest.setLog(mMyLocation.getLongitude());
			mRequest.setUser_id(mPrefs.getUserID());
			mRequest.setDevice_id(DeviceUtils.getUniqueDeviceID(this));
			Log.e("Nearest Friend::", JsonUtil.writeValue(mRequest));
			new GetNearestFriends().executeOnExecutor(
					AsyncTask.THREAD_POOL_EXECUTOR, mRequest);
		}
	}

	protected void stopRefresh() {
		if (mRefreshItem != null) {
			mRefreshItem.setActionView(null);
		}
	}

	protected void runRefresh() {
		if (mRefreshItem != null) {
			mRefreshItem
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
					mLvNearestPeople.setAdapter(mAdapter);
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

	ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBounded = false;
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mBounded = true;
			@SuppressWarnings("unchecked")
			LocalBinder<AddressSyncService> mLocalBinder = (LocalBinder<AddressSyncService>) service;
			mService = (AddressSyncService) mLocalBinder.getService();
		}
	};

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

			return mGpsUtils.getLocationFromProvider();
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (result instanceof Location) {
				mMyLocation = (Location) result;
				if (mMyLocation == null) {
					new GetMyLocation()
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					pointMylocation();
					mRequest = new NearestFriendRequest();
					mRequest.setLat(mMyLocation.getLatitude());
					mRequest.setLog(mMyLocation.getLongitude());
					mRequest.setUser_id(mPrefs.getUserID());
					new GetNearestFriends().executeOnExecutor(
							AsyncTask.THREAD_POOL_EXECUTOR, mRequest);
				}
			}
		}

	}
}
