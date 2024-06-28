package com.app.flex.tooling.utils;

import org.apache.commons.lang3.StringUtils;

public class CommonUtils {

	private CommonUtils() {}
	
	public static String getPersonId(String prefix, long series, int length) {
		int fixedLen = prefix.length()+String.valueOf(series).length();
		String sequenceNumber = StringUtils.repeat("0", length-fixedLen).concat(String.valueOf(series));
		return prefix.concat(sequenceNumber);
	}
}
