package com.findingfriends.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.Locale;

/**
 * Utility class for accessing various device features such as device id,
 * telephone number etc.
 * 
 * 
 */
public class DeviceUtils {

	/**
	 * returns the cell phone number from line 1
	 * 
	 * @param context
	 * @return
	 */
	public static String getTelephoneNumber(Context context) {
		TelephonyManager tMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tMgr.getLine1Number();
	}

	/**
	 * Gets the country ISO from telephony network
	 * 
	 * @param context
	 * @return
	 */
	public static String getCountryIso(Context context) {
		TelephonyManager tMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String country = tMgr.getNetworkCountryIso();
		if (country != null) {
			if (!country.isEmpty())
				return country.toUpperCase(Locale.getDefault());
			else {
				country = Locale.getDefault().getCountry();
				// return country;
				return "NP"; // Need to comment out and uncomment the above line
								// of code
			}
		}
		return null;
	}
}
