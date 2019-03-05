package com.dbs.loyalty.util;

import org.apache.commons.codec.binary.Base64;

import com.dbs.loyalty.config.Constant;

public class Base64Util {

	public static String getString(byte[] bytes) {
		if (bytes != null) {
			return String.format(Constant.BASE64_FORMAT, new String(Base64.encodeBase64String(bytes)));
		}else {
			return null;
		}
	}
}
