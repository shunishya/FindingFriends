package com.findingfriends.fragments;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.findingfriends.activities.MainActivity;
import com.findingfriends.services.AddressSyncService;
import com.findingfriends.utils.GPSUtils;
import com.findings.findingfriends.R;

public class SplashFragment extends SherlockFragment {
	private MainActivity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_splash, container,
				false);
		getSherlockActivity().getSupportActionBar().hide();
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = (MainActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		final GPSUtils gpsUtils = new GPSUtils(getSherlockActivity());
		if (!gpsUtils.isProviderEnable()) {
			gpsUtils.showSettingsAlert(LocationManager.GPS_PROVIDER);
		}else{
		Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {

				if (mActivity.isUserLoggedIn()) {
					getSherlockActivity().startService(
							new Intent(getSherlockActivity(),
									AddressSyncService.class));
					mActivity.gotoMainScreen();

				} else {
					mActivity.gotoRegisterView();
				}
			}
		};
		handler.postDelayed(r, 2000);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mActivity.isUserLoggedIn()) {
			getSherlockActivity().startService(
					new Intent(getSherlockActivity(),
							AddressSyncService.class));
			mActivity.gotoMainScreen();

		} else {
			mActivity.gotoRegisterView();
		}
	}

}
