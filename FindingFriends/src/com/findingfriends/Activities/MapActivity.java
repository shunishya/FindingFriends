package com.findingfriends.activities;

import im.dino.dbinspector.activities.DbInspectorActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.example.findingfriends.R;
import com.findingfriends.adapter.NearestPeopleAdapter;
import com.findingfriends.dummyvalue.DummyContacts;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MapActivity extends SherlockActivity implements OnClickListener {
	private GoogleMap map;
	private ListView lvNearestPeople;
	private Button btnNavigate, btnAway;
	private NearestPeopleAdapter mAdapetr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_map_activity);
		// map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		// .getMap();
		lvNearestPeople = (ListView) findViewById(R.id.lvNearestPeople);
		btnNavigate = (Button) findViewById(R.id.btnNavigate);
		btnAway = (Button) findViewById(R.id.btnAway);
		mAdapetr = new NearestPeopleAdapter(this, DummyContacts.getContacts());
		lvNearestPeople.setAdapter(mAdapetr);
		btnNavigate.setOnClickListener(this);
		btnAway.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnNavigate:
			startActivity(new Intent(MapActivity.this, NavigateActivity.class));
			break;
		case R.id.btnAway:
			startActivity(new Intent(MapActivity.this, PeopleInGroup.class));
			break;

		default:
			break;
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
			startActivity(new Intent(MapActivity.this,
					DbInspectorActivity.class));
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
