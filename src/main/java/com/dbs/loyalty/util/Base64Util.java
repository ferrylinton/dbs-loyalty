package com.dbs.loyalty.util;

import org.apache.commons.codec.binary.Base64;

import com.dbs.loyalty.config.Constant;

public class Base64Util {

	public static String getString(byte[] bytes) {
		if (bytes != null) {
			return Constant.BASE64_PREFIX + new String(Base64.encodeBase64String(bytes));
		}else {
			return null;
		}
	}
	
	public static byte[] getBytes(String base64String) {
		if (base64String != null) {
			base64String = base64String.replace(Constant.BASE64_PREFIX, Constant.EMPTY);
			return Base64.decodeBase64(base64String);
		}else {
			return null;
		}
	}
	
}
