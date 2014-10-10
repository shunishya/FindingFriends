package com.findingfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.findingfriends.interfaces.AdapterToActivity;
import com.findingfriends.models.ContactModel;
import com.findings.findingfriends.R;

import java.util.ArrayList;

public class DummyAdapter extends ArrayAdapter<ContactModel>
implements OnClickListener {
private Context mContext;
private LayoutInflater mInflater;
private ViewHolder holder = null;
private ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
private AdapterToActivity mAdapterToActivity;

public DummyAdapter(Context context,
	ArrayList<ContactModel> objects, AdapterToActivity mActivity) {
super(context, R.layout.nearest_people_row, R.id.tvPeople, objects);
this.mContext = context;
this.mInflater = LayoutInflater.from(mContext);
this.mAdapterToActivity = mActivity;
contactList.addAll(objects);
}

@Override
public int getCount() {
// TODO Auto-generated method stub
return contactList.size();
}

@Override
public ContactModel getItem(int position) {
// TODO Auto-generated method stub
return contactList.get(position);
}

@Override
public View getView(int position, View convertView, ViewGroup parent) {
	ContactModel contact = getItem(position);
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

holder.tvPeople.setText(contact.getName());

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
	//mAdapterToActivity.navigate(contactList.get(position));
	break;
case R.id.btnCall:
	mAdapterToActivity.makeACall(contactList.get(position).getPhonenumber());
	break;
case R.id.btnMsg:
	mAdapterToActivity.sendMsg(contactList.get(position).getPhonenumber());
	break;

default:
	break;
}

}

}
