package com.findingfriends.Activities;

import im.dino.dbinspector.activities.DbInspectorActivity;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewConfiguration;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.findingfriends.R;
import com.findingfriends.services.AddressSyncService;

public class MainActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
			getViewConfiguration();
		}
		startService(new Intent(this, AddressSyncService.class));
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

}
