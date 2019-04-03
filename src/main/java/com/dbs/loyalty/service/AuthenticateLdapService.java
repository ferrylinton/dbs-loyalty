package com.dbs.loyalty.service;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.AuthenticationLdapProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticateLdapService {
	
	private final LdapTemplate ldapTemplate;

    private final AuthenticationLdapProperties applicationLdapProperties;
    
    public boolean authenticate(String username, String password) {
    	EqualsFilter filter = new EqualsFilter(applicationLdapProperties.getAttribute(), getUsername(username));
    	return ldapTemplate.authenticate(LdapUtils.emptyLdapName(), filter.toString(), password);
    }
    
    private String getUsername(String username) {
    	if(applicationLdapProperties.getDomain() == null) {
    		return username;
    	}else {
    		return username + "@" + applicationLdapProperties.getDomain();
    	}
    }
    
}
