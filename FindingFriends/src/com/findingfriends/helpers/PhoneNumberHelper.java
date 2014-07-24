package com.findingfriends.helpers;

import java.util.Locale;

import com.findingfriends.models.ContactModel;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

public class PhoneNumberHelper {

	private PhoneNumberUtil mPhoneUtil;

	public PhoneNumberHelper() {
		mPhoneUtil = PhoneNumberUtil.getInstance();
	}

	public boolean isValidPhoneNumber(String phoneNumberString,
			String countryCode) {
		try {
			PhoneNumber phoneNumber = mPhoneUtil.parse(phoneNumberString,
					countryCode);
			if (mPhoneUtil.isValidNumber(phoneNumber)) {
				return true;
			}
		} catch (NumberParseException e) {
			// return false;
		}
		return false;
	}

	public String getPhoneNumberIfValid(String phoneNumberString,
			String countryCode) {
		try {
			PhoneNumber phoneNumber = mPhoneUtil.parse(phoneNumberString,
					countryCode);
			if (mPhoneUtil.isValidNumber(phoneNumber)) {
				return String.valueOf(phoneNumber.getNationalNumber());
			}
		} catch (NumberParseException e) {
			// return false;
		}
		return null;
	}

	public ContactModel getContactNumberCountry(String phoneNumberFromDevice,
			String countryString) {
		if (countryString != null)
			countryString = countryString.toUpperCase(Locale.US);
		ContactModel contact = new ContactModel();
		try {
			PhoneNumber phoneNumber = mPhoneUtil.parse(phoneNumberFromDevice,
					countryString);
			if (mPhoneUtil.isValidNumber(phoneNumber)) {
				contact.setPhonenumber(mPhoneUtil
						.getNationalSignificantNumber(phoneNumber));
				return contact;
			}
		} catch (NumberParseException e) {

		}
		return null;
	}

	public String getFormatedPhoneNumber(String phoneNumberString,
			String countryCode) {
		try {
			PhoneNumber phoneNumber = mPhoneUtil.parse(phoneNumberString,
					countryCode);
			String formatedNumber = mPhoneUtil.format(phoneNumber,
					PhoneNumberFormat.NATIONAL);
			return formatedNumber;
		} catch (NumberParseException e) {
			// return false;
		}
		return null;
	}

}
