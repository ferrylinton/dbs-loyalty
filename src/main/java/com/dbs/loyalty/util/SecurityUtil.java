package com.dbs.loyalty.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {
 
	public static String getCurrentEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
    }
	
	public static List<String> getAuthorities() {
		List<String> result = new ArrayList<>();
		
		for(Object obj : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if(obj instanceof SimpleGrantedAuthority) {
				SimpleGrantedAuthority authority = (SimpleGrantedAuthority) obj;
				result.add(authority.getAuthority());
			}
        }
		
		return result;
    }
	
}
