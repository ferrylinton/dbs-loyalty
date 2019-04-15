package com.dbs.loyalty.security.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dbs.loyalty.domain.User;


public class WebAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;
	
	private static final String ROLE_CUSTOMER = "CUSTOMER";
	
	private String principal;
	
	private String credentials;
	
	private String details;
	
	private boolean authenticated; 
	
	private User user;
	
	public WebAuthentication(User user) {
		this.principal = user.getUsername();
		this.user = user;
		this.authenticated = true; 
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(ROLE_CUSTOMER));
        return authorities;
    }

	@Override
	public Object getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	@Override
	public Object getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Override
	public Object getPrincipal() {
		return getName();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean b){
		this.authenticated = b;
	}

	@Override
	public String getName() {
		return principal;
	}

	public User getUser() {
		return user;
	}

}
