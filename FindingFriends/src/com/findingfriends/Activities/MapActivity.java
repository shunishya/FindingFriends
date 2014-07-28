package com.findingfriends.Activities;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.example.findingfriends.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class MapActivity extends SherlockActivity implements OnClickListener{
	private GoogleMap map;
	private ListView lvNearestPeople;
	private Button btnNavigate, btnAway;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_map_activity);
//		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
//				.getMap();
		lvNearestPeople=(ListView)findViewById(R.id.lvNearestPeople);
		btnNavigate=(Button)findViewById(R.id.btnNavigate);
		btnAway=(Button)findViewById(R.id.btnAway);
		btnNavigate.setOnClickListener(this);
		btnAway.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
