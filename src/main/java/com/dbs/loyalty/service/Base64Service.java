package com.dbs.loyalty.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class Base64Service {

	private static final String BASE64_FORMAT = "data:%s;base64,%s";
	
	public static String getString(byte[] imageBytes, String imageContentType) {
		return String.format(BASE64_FORMAT, imageContentType, Base64.encodeBase64String(imageBytes));
	}
	
}
