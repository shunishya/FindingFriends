package com.findingfriends.api;


public class FindingFriendsException extends Exception {
	private static final long serialVersionUID = 1L;
	private String exception;

	FindingFriendsException(String s) {
		exception = s;
	}

	public FindingFriendsException(String prefix, int errorCode) {
		exception = prefix;
		switch (errorCode) {
		case 406:
			exception += " already exists.";
			break;
		}
	}

	public String toString() {
		return ("Exception " + exception);
	}

}