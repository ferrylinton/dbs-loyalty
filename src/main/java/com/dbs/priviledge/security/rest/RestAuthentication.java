package com.dbs.priviledge.security.rest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.domain.Customer;
import com.dbs.priviledge.model.Login;


public class RestAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;
	
	private Object principal;
	
	private Object credentials;
	
	private String details;
	
	private boolean authenticated; 
	
	private Customer customer;

	public RestAuthentication(String principal) {
		this.principal = principal;
		this.credentials = null;
		this.authenticated = true;
	}
	
	public RestAuthentication(Login login) {
		this.principal = login.getEmail();
		this.credentials = login.getPassword();
		this.authenticated = false;
	}
	
	public RestAuthentication(String principal, String credentials, Customer customer) {
		this.principal = principal;
		this.credentials = credentials;
		this.customer = customer;
		this.authenticated = true; 
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(Constant.ROLE_CUSTOMER));
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

	public Customer getCustomer() {
		return customer;
	}

}
