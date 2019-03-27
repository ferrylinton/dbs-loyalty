package com.dbs.loyalty.ldap.service;

import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.AuthenticationLdapProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserLdapService {
	
	private String OBJECT_CLASS = "objectclass";
	
	private String PERSON = "person";

	private final LdapTemplate ldapTemplate;
    
    private final LdapProperties ldapProperties;
    
    private final AuthenticationLdapProperties applicationLdapProperties;
    
    public boolean authenticate(String username, String password) {
    	AndFilter filter = new AndFilter()
        	.and(new EqualsFilter(OBJECT_CLASS, PERSON))
        	.and(new EqualsFilter(applicationLdapProperties.getAttribute(), username));
    	
    	return ldapTemplate.authenticate(ldapProperties.getBase(), filter.toString(), password);
    }
    
}
