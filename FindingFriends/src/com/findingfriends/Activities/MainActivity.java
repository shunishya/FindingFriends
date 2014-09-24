package com.findingfriends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewConfiguration;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.findingfriends.fragments.RegisterFragment;
import com.findingfriends.fragments.SplashFragment;
import com.findingfriends.utils.FindingFriendsPreferences;
import com.findings.findingfriends.R;

import java.lang.reflect.Field;

public class MainActivity extends SherlockFragmentActivity {
	private RegisterFragment mRegisterFragment;
	private FindingFriendsPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().hide();
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
			getViewConfiguration();
		}
		mPrefs = new FindingFriendsPreferences(MainActivity.this);

		mRegisterFragment = new RegisterFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.main_activity, new SplashFragment()).commit();
	}

	/**
	 * call on onCreate
	 * 
	 * Enables the overflow menu.
	 * 
	 * */
	public void getViewConfiguration() {
		try {
			ViewConfiguration config = ViewConfiguration.get(MainActivity.this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isUserLoggedIn() {
		return mPrefs.isUserLoggedIn();
		// return false;
	}

	public void gotoMainScreen() {
		FindingFriends.startService(getApplicationContext());
		startActivity(new Intent(this, MapActivity.class));
		finish();
	}

	public void gotoRegisterView() {
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_left,
						R.anim.slide_out_right)
				.replace(R.id.main_activity, mRegisterFragment).commit();
	}

}
