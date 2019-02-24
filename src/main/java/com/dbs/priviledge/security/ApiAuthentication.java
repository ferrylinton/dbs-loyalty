package com.dbs.priviledge.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dbs.priviledge.domain.Authority;
import com.dbs.priviledge.domain.User;


public class ApiAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;
	
	private Object principal;
	
	private Object credentials;
	
	private String details;
	
	private boolean authenticated; 
	
	private User user;

	public ApiAuthentication(Object principal, Object credentials) {
		this.principal = principal;
		this.credentials = credentials;
		this.authenticated = false;
	}
	
	public ApiAuthentication(String principal, String credentials, User user) {
		this.principal = principal;
		this.credentials = credentials;
		this.user = user;
		this.authenticated = true; 
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		for(Authority authority : user.getRole().getAuthorities()){
			authorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
		
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
		return principal;
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
		return (String) principal;
	}

	public User getUser() {
		return user;
	}

}
