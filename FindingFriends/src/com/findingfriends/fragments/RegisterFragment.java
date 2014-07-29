package com.findingfriends.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.example.findingfriends.R;
import com.findingfriends.Activities.MainActivity;
import com.findingfriends.api.FindingFriendsApi;
import com.findingfriends.api.FindingFriendsException;
import com.findingfriends.api.models.RegisterRequest;
import com.findingfriends.api.models.RegisterResponse;
import com.findingfriends.helpers.PhoneNumberHelper;
import com.findingfriends.utils.DeviceUtils;
import com.findingfriends.utils.JsonUtil;

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

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDia = new ProgressDialog(getSherlockActivity());
			progressDia.setTitle("Processing");
			progressDia.setMessage("Sending your request....");
			progressDia.show();
		}

		@Override
		protected Object doInBackground(Void... params) {
			FindingFriendsApi api = new FindingFriendsApi(getSherlockActivity());
			RegisterRequest req = new RegisterRequest();
			req.setPhoneNumber(etPhoneNumber.getText().toString());
			req.setUserName(etName.getText().toString());
			req.setGps_lat(10.3673763748848);
			req.setGps_long(85.101010019);
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
					Toast.makeText(getSherlockActivity(), "Register Success",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getSherlockActivity(), "Error",
							Toast.LENGTH_SHORT).show();
				}

			} else if (result instanceof FindingFriendsException) {
				Toast.makeText(getSherlockActivity(), "Exception",
						Toast.LENGTH_SHORT).show();
			}

		}

	}

}
