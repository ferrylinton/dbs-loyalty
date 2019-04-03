package com.dbs.loyalty.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

	public static String getLogged() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
    }
	
	public static List<String> getAuthorities() {
		List<String> result = new ArrayList<>();
		
		for(GrantedAuthority obj : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if(obj instanceof SimpleGrantedAuthority) {
				SimpleGrantedAuthority authority = (SimpleGrantedAuthority) obj;
				result.add(authority.getAuthority());
			}
        }
		
		return result;
    }

	private SecurityUtil() {
		// hide constructor
	}
	
}
