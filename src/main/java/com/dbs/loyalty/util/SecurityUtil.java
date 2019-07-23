package com.dbs.loyalty.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.security.rest.RestAuthentication;
import com.dbs.loyalty.security.web.WebAuthentication;

public final class SecurityUtil {

	public static String getLogged() {
		if(SecurityContextHolder.getContext().getAuthentication() != null) {
			return SecurityContextHolder.getContext().getAuthentication().getName();
		}else {
			return null;
		}
    }
	
	public static String getId() {
		if(SecurityContextHolder.getContext().getAuthentication() instanceof RestAuthentication) {
			RestAuthentication authentication = (RestAuthentication) SecurityContextHolder.getContext().getAuthentication();
			return authentication.getId();
		}else if(SecurityContextHolder.getContext().getAuthentication() instanceof RestAuthentication) {
			WebAuthentication authentication = (WebAuthentication) SecurityContextHolder.getContext().getAuthentication();
			return authentication.getUser().getId();
		}
		
		return null;
    }
	
	public static Customer getCustomer() {
		if(SecurityContextHolder.getContext().getAuthentication() instanceof RestAuthentication) {
			RestAuthentication authentication = (RestAuthentication) SecurityContextHolder.getContext().getAuthentication();
			Customer customer = new Customer();
			customer.setId(authentication.getId());
			customer.setEmail(authentication.getName());
			return customer;
		}
		
		return null;
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
