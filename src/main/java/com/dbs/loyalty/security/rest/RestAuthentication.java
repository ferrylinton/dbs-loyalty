package com.dbs.loyalty.security.rest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dbs.loyalty.domain.cst.Customer;
import com.dbs.loyalty.model.TokenData;


public class RestAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;
	
	private String principal;

	private String details;
	
	private boolean authenticated; 
	
	private Customer customer;
	
	private String id;
	
	private String role;

	public RestAuthentication(TokenData tokenData) {
		this.id = tokenData.getId();
		this.principal = tokenData.getEmail();
		this.role = tokenData.getRole();
		this.authenticated = true;
	}

	public RestAuthentication(TokenData tokenData, Customer customer) {
		this.id = tokenData.getId();
		this.principal = tokenData.getEmail();
		this.role = tokenData.getRole();
		this.customer = customer;
		this.authenticated = true; 
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		authorities.add(new SimpleGrantedAuthority(role));
        return authorities;
    }

	@Override
	public Object getCredentials() {
		return null;
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
	public void setAuthenticated(boolean authenticated){
		this.authenticated = authenticated;
	}

	@Override
	public String getName() {
		return principal;
	}

	public Customer getCustomer() {
		return customer;
	}

	public String getId() {
		return id;
	}

	public String getRole() {
		return role;
	}

}
