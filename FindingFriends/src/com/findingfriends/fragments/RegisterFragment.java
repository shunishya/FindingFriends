package com.findingfriends.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
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
import com.findingfriends.activities.MainActivity;
import com.findingfriends.api.FindingFriendsApi;
import com.findingfriends.api.FindingFriendsException;
import com.findingfriends.api.models.RegisterRequest;
import com.findingfriends.api.models.RegisterResponse;
import com.findingfriends.helpers.PhoneNumberHelper;
import com.findingfriends.services.AddressSyncService;
import com.findingfriends.utils.DeviceUtils;
import com.findingfriends.utils.FindingFriendsPreferences;
import com.findingfriends.utils.GPSUtils;
import com.findingfriends.utils.JsonUtil;
import com.findings.findingfriends.R;

public class RegisterFragment extends SherlockFragment implements
		OnClickListener {
	private MainActivity mActivity;
	private EditText mEtName;
	private EditText mEtPhoneNumber;
	private EditText mEtPassword;
	private Button mBtnSubmit;
	private ProgressDialog mDialog;
	private GPSUtils mGpsUtils;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_register, container,
				false);
		getSherlockActivity().getSupportActionBar().show();
		mEtName = (EditText) rootView.findViewById(R.id.etName);
		mEtPhoneNumber = (EditText) rootView.findViewById(R.id.etPhoneNumber);
		mEtPassword = (EditText) rootView.findViewById(R.id.etPassword);
		mBtnSubmit = (Button) rootView.findViewById(R.id.btnSubmit);
		mGpsUtils = new GPSUtils(getSherlockActivity());
		mEtPhoneNumber.setOnEditorActionListener(new OnEditorActionListener() {

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
		mBtnSubmit.setOnClickListener(this);
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
		String name = mEtName.getText().toString();
		String phone = mEtPhoneNumber.getText().toString();
		mDialog = ProgressDialog.show(mActivity, "Processing",
				"Verifying input");
		mDialog.setCancelable(false);
		if (!name.isEmpty() && !phone.isEmpty()) {

			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					String inputNumber = mEtPhoneNumber.getText().toString();
					if (!inputNumber.isEmpty()) {
						PhoneNumberHelper pnHelper = new PhoneNumberHelper();
						final String phoneNumber = pnHelper
								.getPhoneNumberIfValid(inputNumber, DeviceUtils
										.getCountryIso(getSherlockActivity()));

						if (phoneNumber != null) {
							mActivity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									mEtPhoneNumber.setText(phoneNumber);
									mDialog.dismiss();
									// TODO call async class for submit activity
									new RegisterUser()
											.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
			mDialog.cancel();
			Toast.makeText(getSherlockActivity(), "Please enter your name.",
					Toast.LENGTH_SHORT).show();

		} else {
			mDialog.cancel();
			Toast.makeText(getSherlockActivity(), "Please enter your phone.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View v) {
		onSubmit();
	}

	public class RegisterUser extends AsyncTask<Void, String, Object> {
		String results;
		String request;
		private ProgressDialog progressDia;
		private Location loc;
		FindingFriendsPreferences mPrefs = new FindingFriendsPreferences(
				getSherlockActivity());

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDia = new ProgressDialog(getSherlockActivity());
			progressDia.setTitle("Processing");
			progressDia.setMessage("Sending your request....");
			progressDia.setCancelable(false);
			progressDia.show();
		}

		@Override
		protected Object doInBackground(Void... params) {

			FindingFriendsApi api = new FindingFriendsApi(getSherlockActivity());
			RegisterRequest req = new RegisterRequest();
			req.setPhoneNumber(mEtPhoneNumber.getText().toString());
			req.setUserName(mEtName.getText().toString());
			req.setPassword(mEtPassword.getText().toString());
			loc = mGpsUtils.getLocationFromProvider();
			if(loc!=null){
			req.setGps_lat(loc.getLatitude());
			req.setGps_long(loc.getLongitude());
			}
			request = JsonUtil.writeValue(req);
			try {
				return api.sendRegisterRequest(req);
			} catch (FindingFriendsException e) {
				e.printStackTrace();
				return e;
			}
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			if (progressDia.isShowing())
				progressDia.dismiss();
			if (result instanceof RegisterResponse) {
				RegisterResponse response = (RegisterResponse) result;
				if (!response.isError()) {
					mPrefs.setUserID(response.getUser_id());
					Toast.makeText(getSherlockActivity(), "Register Success",
							Toast.LENGTH_SHORT).show();
					getSherlockActivity().startService(
							new Intent(getSherlockActivity(),
									AddressSyncService.class));
					mActivity.gotoMainScreen();
				} else {
					Toast.makeText(getSherlockActivity(), response.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			} else if (result instanceof FindingFriendsException) {
				Toast.makeText(getSherlockActivity(), "Exception",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

}
