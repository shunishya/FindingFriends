package com.findingfriends.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.findingfriends.R;
import com.findingfriends.activities.MainActivity;
import com.findingfriends.services.AddressSyncService;

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
		Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {
				if (mActivity.isUserLoggedIn()) {
					getSherlockActivity().startService(new Intent(getSherlockActivity(), AddressSyncService.class));
					mActivity.gotoMainScreen();
					
				} else{
					mActivity.gotoRegisterView();
				}
			}
		};
		handler.postDelayed(r, 2000);
	}

}
