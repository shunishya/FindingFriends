package com.findingfriends.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
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
			startActivity(new Intent(MapActivity.this,NavigateActivity.class));
			break;

		default:
			break;
		}
	}

}
