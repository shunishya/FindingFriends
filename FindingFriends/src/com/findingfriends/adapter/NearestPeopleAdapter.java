package com.findingfriends.adapter;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.findingfriends.interfaces.AdapterToActivity;
import com.findingfriends.models.UserWithDistance;
import com.findingfriends.utils.GPSUtils;
import com.findings.findingfriends.R;

import java.util.ArrayList;

public class NearestPeopleAdapter extends ArrayAdapter<UserWithDistance>
		implements OnClickListener {
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder mHolder = null;
	private ArrayList<UserWithDistance> mContactList = new ArrayList<UserWithDistance>();
	private AdapterToActivity mAdapterToActivity;

	public NearestPeopleAdapter(Context context,
			ArrayList<UserWithDistance> objects, AdapterToActivity mActivity) {
		super(context, R.layout.nearest_people_row, R.id.tvPeople, objects);
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mAdapterToActivity = mActivity;
		mContactList.addAll(objects);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mContactList.size();
	}

	@Override
	public UserWithDistance getItem(int position) {
		// TODO Auto-generated method stub
		return mContactList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserWithDistance contact = getItem(position);
		mHolder = new ViewHolder();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.nearest_people_row, null,
					false);
			mHolder.tvPeople = (TextView) convertView
					.findViewById(R.id.tvPeople);
			mHolder.tvAddress = (TextView) convertView
					.findViewById(R.id.tvAddress);
			mHolder.btnCall = (Button) convertView.findViewById(R.id.btnCall);
			mHolder.btnMsg = (Button) convertView.findViewById(R.id.btnMsg);
			mHolder.btnNavigate = (Button) convertView
					.findViewById(R.id.btnNavigate);
			mHolder.btnCall.setTag(position);
			mHolder.btnMsg.setTag(position);
			mHolder.btnNavigate.setTag(position);
			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
			mHolder.btnCall.setTag(position);
			mHolder.btnMsg.setTag(position);
			mHolder.btnNavigate.setTag(position);
		}
		Location location = new Location("Address");
		mHolder.tvPeople.setText(contact.getUser().getUserName());
		location.setLatitude(contact.getUser().getGps_lat());
		location.setLongitude(contact.getUser().getGps_long());
		long diff = System.currentTimeMillis() - contact.getUser().getTime();
		if ((diff / 1000) < 60) {
			mHolder.tvAddress.setText("At" + GPSUtils.getAddress(location) + " "
					+ "(" + (diff / 1000) + "seconds ago)");
		} else if ((diff / 60000) < 60) {
			double min = diff / 60000;
			mHolder.tvAddress.setText("At" + GPSUtils.getAddress(location) + " "
					+ "(" + min + "minutes ago)");
		} else {
			double hrs = diff / (1000 * 60 * 60);
			mHolder.tvAddress.setText("At " + GPSUtils.getAddress(location)
					+ " " + "(" + hrs + "hours ago)");
		}
		mHolder.btnNavigate.setOnClickListener(this);
		mHolder.btnCall.setOnClickListener(this);
		mHolder.btnMsg.setOnClickListener(this);
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
			mAdapterToActivity.navigate(mContactList.get(position));
			break;
		case R.id.btnCall:
			mAdapterToActivity.makeACall(mContactList.get(position).getUser()
					.getPhoneNumber());
			break;
		case R.id.btnMsg:
			mAdapterToActivity.sendMsg(mContactList.get(position).getUser()
					.getPhoneNumber());
			break;

		default:
			break;
		}

	}
}
