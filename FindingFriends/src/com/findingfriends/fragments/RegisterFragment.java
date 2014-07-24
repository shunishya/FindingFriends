package com.findingfriends.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.example.findingfriends.R;
import com.findingfriends.Activities.MainActivity;
import com.findingfriends.helpers.PhoneNumberHelper;
import com.findingfriends.utils.DeviceUtils;

public class RegisterFragment extends SherlockFragment implements
		OnClickListener {
	private MainActivity mActivity;
	private EditText etName;
	private EditText etPhoneNumber;
	private Button btnSubmit;
	private ProgressDialog mDialog;

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
		mDialog = ProgressDialog.show(mActivity, "Processing",
				"Verifying input");
		if (!name.isEmpty() && !phone.isEmpty()) {

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					String inputNumber = etPhoneNumber.getText().toString();
					if (!inputNumber.isEmpty()) {
						PhoneNumberHelper pnHelper = new PhoneNumberHelper();
						final String phoneNumber = pnHelper
								.getPhoneNumberIfValid(inputNumber, DeviceUtils
										.getCountryIso(getSherlockActivity()));
						if (phoneNumber != null) {
							mActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									etPhoneNumber.setText(phoneNumber);
									mDialog.dismiss();
									// TODO call async class for submit activity
								}
							});
						} else {
							mActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(mActivity,
											"Invalid phone number.",
											Toast.LENGTH_SHORT).show();
									if (mDialog.isShowing())
										mDialog.cancel();
								}
							});

						}
					} else {
						mActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(mActivity,
										"Phone number empty.",
										Toast.LENGTH_SHORT).show();
								if (mDialog.isShowing())
									mDialog.cancel();
							}
						});
					}
				}
			};
			new Thread(runnable).start();

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
