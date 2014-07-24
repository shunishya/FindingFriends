package com.findingfriends.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.findingfriends.R;
import com.findingfriends.Activities.MainActivity;

public class RegisterFragment extends SherlockFragment implements
		OnClickListener {
	private MainActivity mActivity;
	private EditText etName;
	private EditText etPhoneNumber;
	private Button btnSubmit;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_register, container,
				false);
		getSherlockActivity().getSupportActionBar().show();
		etName = (EditText) rootView.findViewById(R.id.etName);
		etPhoneNumber = (EditText) rootView.findViewById(R.id.etPhoneNumber);
		btnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
		etPhoneNumber.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					onSubmit();
					return true;
				}
				return false;
			}
		});
		btnSubmit.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = (MainActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		Handler handler = new Handler();
		final Runnable r = new Runnable() {
			public void run() {

			}
		};
		handler.post(r);
	}

	public void onSubmit() {
		String name = etName.getText().toString();
		String phone = etPhoneNumber.getText().toString();
		if (!name.isEmpty() && !phone.isEmpty()) {
			// TODO call async class for submit activity

		} else if (name.isEmpty()) {
			Toast.makeText(getSherlockActivity(), "Please enter your name.",
					Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(getSherlockActivity(), "Please enter your phone.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		onSubmit();
	}

}
