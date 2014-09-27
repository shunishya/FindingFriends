package com.findingfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.findingfriends.interfaces.ContactAttachDetachListner;
import com.findingfriends.utils.ContactListSelectableItem;
import com.findings.findingfriends.R;

import java.util.List;

public class SearchContactsAdapter extends
		ArrayAdapter<ContactListSelectableItem> {
	private LayoutInflater mInflater;
	private List<ContactListSelectableItem> mContacts;
	private ContactAttachDetachListner mContactAttachDetachListener;

	public SearchContactsAdapter(Context context,
			List<ContactListSelectableItem> imnContacts,
			ContactAttachDetachListner listener) {
		super(context, R.id.tvContactName, imnContacts);
		this.mInflater = LayoutInflater.from(context);
		this.mContacts = imnContacts;
		this.mContactAttachDetachListener = listener;
	}

	@Override
	public int getCount() {
		return mContacts.size();
	}

	@Override
	public ContactListSelectableItem getItem(int position) {
		return mContacts.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final ContactListSelectableItem item = getItem(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.select_contact_row, null);
			holder = new ViewHolder();
			holder.tvContactName = (TextView) convertView
					.findViewById(R.id.tvContactName);
			holder.btnAddOrRemove = (RadioButton) convertView
					.findViewById(R.id.rdSelect);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (item.isSelected()) {
			holder.btnAddOrRemove.setChecked(true);
		} else {
			holder.btnAddOrRemove.setChecked(false);
		}

		holder.tvContactName.setText(item.getContact().getName());
		holder.btnAddOrRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View clickedView) {
				RadioButton button = (RadioButton) clickedView;
				if (item.isSelected()) {
					item.setSelected(false);
					button.setChecked(false);
				} else {
					item.setSelected(true);
					button.setChecked(true);
				}
				mContactAttachDetachListener.clickedUser(item);
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		TextView tvContactName;
		RadioButton btnAddOrRemove;
	}

	public void setCheck(ContactListSelectableItem item) {
		if (item.isSelected()) {
			item.setSelected(false);
		} else {
			item.setSelected(true);
		}
		mContactAttachDetachListener.clickedUser(item);

	}

}
