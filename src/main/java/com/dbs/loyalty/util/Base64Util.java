package com.dbs.loyalty.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	public static String getString(byte[] bytes) {
		if (bytes != null) {
			return Base64.encodeBase64String(bytes);
		}else {
			return null;
		}
	}
	
	public static byte[] getBytes(String base64String) {
		if (base64String != null) {
			return Base64.decodeBase64(base64String);
		}else {
			return new byte[] {};
		}
	}

	private Base64Util(){
		// hide constructor
	}
	
}
