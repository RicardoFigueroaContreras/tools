package com.rfigueroa.codegenerator.commons;

import org.springframework.util.ObjectUtils;

public final class StringUtil {

	private StringUtil() {
		
	}
	
	public static String correctName(String name) {
		
		if (!ObjectUtils.isEmpty(name)) {
			return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
		}

		return null;
	}
	
	public static String camelName(String name) {
		
		if (!ObjectUtils.isEmpty(name)) {
			return name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
		}

		return null;
	}
	
	public static String getMethodName(String name) {
		
		if (!ObjectUtils.isEmpty(name)) {
			return name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
		}

		return null;
	}
	public static String lower(String srt) {
		return srt.toLowerCase();
	}
	
	public static String upper(String srt) {
		return srt.toUpperCase();
	}
}
