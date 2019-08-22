package com.dbs.loyalty.service;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

@Service
public class Base64Service {

	private static final String BASE64_FORMAT = "data:png;base64,%s";
	
	public static String getString(byte[] imageBytes) {
		return String.format(BASE64_FORMAT, Base64.encodeBase64String(imageBytes));
	}
	
}
