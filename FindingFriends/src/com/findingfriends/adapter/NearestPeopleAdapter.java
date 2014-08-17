package com.findingfriends.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.findingfriends.R;
import com.findingfriends.models.ContactModel;
import com.findingfriends.models.UserWithDistance;

public class NearestPeopleAdapter extends ArrayAdapter<UserWithDistance> {
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewHolder holder = null;
	private ArrayList<UserWithDistance> contactList = new ArrayList<UserWithDistance>();

	public NearestPeopleAdapter(Context context, ArrayList<UserWithDistance> objects) {
		super(context, R.layout.nearest_people_row, R.id.tvPeople, objects);
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
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
			holder.btnCall = (Button) convertView.findViewById(R.id.btnCall);
			holder.btnMsg = (Button) convertView.findViewById(R.id.btnMsg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvPeople.setText(contact.getUser().getUserName());
		return convertView;
	}

	public class ViewHolder {
		private TextView tvPeople;
		private Button btnCall;
		private Button btnMsg;
	}
}
