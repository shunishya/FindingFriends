package com.findingfriends.Activities;

import im.dino.dbinspector.activities.DbInspectorActivity;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewConfiguration;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.findingfriends.R;
import com.findingfriends.fragments.RegisterFragment;
import com.findingfriends.fragments.SplashFragment;
import com.findingfriends.services.AddressSyncService;
import com.findingfriends.utils.FindingFriendsPreferences;

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

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
			startActivity(new Intent(MainActivity.this,
					DbInspectorActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean isUserLoggedIn() {
		return mPrefs.isUserLoggedIn();
		//return false;
	}

	public void gotoMainScreen() {
		startActivity(new Intent(this,MapActivity.class));
	}

	public void gotoRegisterView() {
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(R.anim.slide_in_left,
						R.anim.slide_out_right)
				.replace(R.id.main_activity, mRegisterFragment).commit();
	}

}
