package com.findingfriends.api;

/**
 * Exception with custom messages for different types of exception handling
 * within the ILoop Messaging App
 * 
 * @author tektak
 * 
 */

public class FindingFriendsException extends Exception {
	private static final long serialVersionUID = 1L;
	private String exeption;

	FindingFriendsException(String s) {
		exeption = s;
	}

	public FindingFriendsException(String prefix, int errorCode) {
		exeption = prefix;
		switch (errorCode) {
		case 406:
			exeption += " already exists.";
			break;
		}
	}

	public String toString() {
		return ("ILoopException: " + exeption);
	}

}