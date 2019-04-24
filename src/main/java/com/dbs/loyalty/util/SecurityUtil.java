package com.dbs.loyalty.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.security.rest.RestAuthentication;
import com.dbs.loyalty.security.web.WebAuthentication;

public final class SecurityUtil {

	public static String getLogged() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
    }
	
	public static String getId() {
		if(SecurityContextHolder.getContext().getAuthentication() instanceof RestAuthentication) {
			RestAuthentication authentication = (RestAuthentication) SecurityContextHolder.getContext().getAuthentication();
			return authentication.getId();
		}
		
		return null;
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
	
	public static boolean hasAuthority(String authorityName) {
		for(GrantedAuthority obj : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
			if(obj instanceof SimpleGrantedAuthority) {
				SimpleGrantedAuthority authority = (SimpleGrantedAuthority) obj;
				
				if(authority.getAuthority().equals(authorityName)) {
					return true;
				}
			}
        }
		
		return false;
    }
	
	public static User getCurrentUser() {
		if(SecurityContextHolder.getContext().getAuthentication() instanceof WebAuthentication) {
			WebAuthentication webAuthentication = (WebAuthentication) SecurityContextHolder.getContext().getAuthentication();
			return webAuthentication.getUser();
		}else {
			return null;
		}
    }

	private SecurityUtil() {
		// hide constructor
	}
	
}
