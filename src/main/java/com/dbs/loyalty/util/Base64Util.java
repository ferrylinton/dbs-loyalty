package com.dbs.loyalty.util;

import org.apache.commons.codec.binary.Base64;

import com.dbs.loyalty.config.constant.Constant;

public class Base64Util {

	private static final String PATTERN 		= "^data:image/[a-z]+;base64,";
	
	private static final String BASE64_PREFIX	= "data:image/jpg;base64,";
	
	public static String getString(byte[] bytes) {
		if (bytes != null) {
			return BASE64_PREFIX + Base64.encodeBase64String(bytes);
		}else {
			return null;
		}
	}
	
	public static byte[] getBytes(String base64String) {
		if (base64String != null) {
			base64String = base64String.trim().replaceFirst(PATTERN, Constant.EMPTY);
			return Base64.decodeBase64(base64String.trim());
		}else {
			return new byte[] {};
		}
	}

	private Base64Util(){
		// hide constructor
	}
}
