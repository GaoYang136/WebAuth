package com.foxmail.gaoyang136.webauth.ulit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUlit {

	public static void main(String[] args) {
		String userName ="你说fdsf123";
		System.out.printf("%s:%b\n", userName, StringUlit.checkUserName(userName));


		String password ="hfASd!_23%#@$.+=";
		System.out.printf("%s:%b\n", password, StringUlit.checkPassword(password));
	}

	public static boolean checkUserName(String userName) {
		boolean valid = true;

		Matcher matcher = Pattern.compile("^\\w+$", Pattern.UNICODE_CHARACTER_CLASS)
				.matcher(userName);

		if (userName.length() > 20) {
			valid = false;
		} else if (!matcher.matches()) {
			valid = false;
		}

		return valid;
	}

	public static boolean checkPassword(String password) {
		boolean valid = true;

		if (password.length() < 6 || password.length() > 20) {
			valid = false;
		} else if (!Pattern.matches("^[A-Za-z\\d!@#$%^&*?+-_\\=\\.]{6,20}$", password)) {
			valid = false;
		}

		return valid;

	}
}
