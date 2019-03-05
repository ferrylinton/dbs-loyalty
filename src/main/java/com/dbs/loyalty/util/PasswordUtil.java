package com.dbs.loyalty.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

	private static PasswordUtil instance;

	private PasswordEncoder passwordEncoder;
	
	private PasswordUtil() {}

	public static PasswordUtil getInstance() {
		if(instance == null) {
			instance = new PasswordUtil();
		}
		
		return instance;
	}
	
	public PasswordEncoder getPasswordEncoder() {
		if(passwordEncoder == null) {
			passwordEncoder = new BCryptPasswordEncoder();
		}
		
        return passwordEncoder;
    }
	
	public String encode(CharSequence rawPassword) {
		return getPasswordEncoder().encode(rawPassword);
	}
	
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return getPasswordEncoder().matches(rawPassword, encodedPassword);
	}
	
}
