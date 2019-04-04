package com.dbs.loyalty.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

	private static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public static String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}
	
	public static boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
	}
	
	private PasswordUtil() {
		// hide constructor
	}
	
}
