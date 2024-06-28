package com.app.flex.tooling.utils;

import java.util.Date;

public class DateConvertor {

	private DateConvertor() {
	}

	public static long dateDifferenceInMinutes(Date eventDate) {
		Date currentDate = new Date();
		long differenceMillis = currentDate.getTime() - eventDate.getTime();
		return differenceMillis / (1000 * 60);
	}
}
