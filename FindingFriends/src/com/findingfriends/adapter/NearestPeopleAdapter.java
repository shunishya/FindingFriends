package com.findingfriends.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.findingfriends.R;
import com.findingfriends.interfaces.AdapterToActivity;
import com.findingfriends.models.UserWithDistance;
import com.findingfriends.utils.GPSUtils;

public class NearestPeopleAdapter extends ArrayAdapter<UserWithDistance>
		implements OnClickListener {
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder holder = null;
	private ArrayList<UserWithDistance> contactList = new ArrayList<UserWithDistance>();
	private AdapterToActivity mAdapterToActivity;

	public NearestPeopleAdapter(Context context,
			ArrayList<UserWithDistance> objects, AdapterToActivity mActivity) {
		super(context, R.layout.nearest_people_row, R.id.tvPeople, objects);
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mAdapterToActivity = mActivity;
		contactList.addAll(objects);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactList.size();
	}

	@Override
	public UserWithDistance getItem(int position) {
		// TODO Auto-generated method stub
		return contactList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserWithDistance contact = getItem(position);
		holder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nearest_people_row, null,
					false);
			holder.tvPeople = (TextView) convertView
					.findViewById(R.id.tvPeople);
			holder.tvAddress = (TextView) convertView
					.findViewById(R.id.tvAddress);
			holder.btnCall = (Button) convertView.findViewById(R.id.btnCall);
			holder.btnMsg = (Button) convertView.findViewById(R.id.btnMsg);
			holder.btnNavigate = (Button) convertView
					.findViewById(R.id.btnNavigate);
			holder.btnCall.setTag(position);
			holder.btnMsg.setTag(position);
			holder.btnNavigate.setTag(position);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.btnCall.setTag(position);
			holder.btnMsg.setTag(position);
			holder.btnNavigate.setTag(position);
		}
		Location location = new Location("Address");
		holder.tvPeople.setText(contact.getUser().getUserName());
		location.setLatitude(contact.getUser().getGps_lat());
		location.setLongitude(contact.getUser().getGps_long());
		long diff = System.currentTimeMillis() - contact.getUser().getTime();
		if (diff / 1000 < 60) {
			holder.tvAddress.setText("At" + GPSUtils.getAddress(location)
					+ " " + "(" + diff + "seconds ago)");
		} else if (diff / 60000 < 60) {
			double min=diff/60000;
			holder.tvAddress.setText("At" + GPSUtils.getAddress(location)
					+ " " + "(" + min + "minutes ago)");
		} else {
			double hrs=diff/(1000*60*60);
			holder.tvAddress.setText("At " + GPSUtils.getAddress(location)
					+ " " + "(" + hrs + "hours ago)");
		}
		holder.btnNavigate.setOnClickListener(this);
		holder.btnCall.setOnClickListener(this);
		holder.btnMsg.setOnClickListener(this);
		return convertView;
	}

	public class ViewHolder {
		private TextView tvPeople;
		private TextView tvAddress;
		private Button btnCall;
		private Button btnMsg;
		private Button btnNavigate;
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		switch (v.getId()) {
		case R.id.btnNavigate:
			mAdapterToActivity.navigate(contactList.get(position));
			break;
		case R.id.btnCall:
			mAdapterToActivity.makeACall(contactList.get(position).getUser()
					.getPhoneNumber());
			break;
		case R.id.btnMsg:
			mAdapterToActivity.sendMsg(contactList.get(position).getUser()
					.getPhoneNumber());
			break;

		default:
			break;
		}

	}
}
