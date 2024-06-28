package com.app.flex.tooling.utils;

import java.security.SecureRandom;

public class OtpGenerator {

	private OtpGenerator() {
	}

	private static final String DIGITS = "0123456789";

	public static String generateOTP(int length) {
		StringBuilder otp = new StringBuilder(length);
		SecureRandom random = new SecureRandom();
		for (int i = 0; i < length; i++) {
			otp.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
		}
		return otp.toString();
	}
}
