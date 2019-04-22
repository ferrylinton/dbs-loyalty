package com.dbs.loyalty.service;

import javax.naming.NamingException;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.AuthenticationLdapProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserLdapService {

	private final LdapTemplate ldapTemplate;
	
	private final AuthenticationLdapProperties applicationLdapProperties;
    
    public boolean isUserExist(String username) {
    	EqualsFilter filter = new EqualsFilter(applicationLdapProperties.getAttribute(), getUsername(username));
    	
    	String result = ldapTemplate.searchForObject(LdapUtils.emptyLdapName(), filter.toString(), new ContextMapper<String>() {

			@Override
			public String mapFromContext(Object ctx) throws NamingException {
				DirContextAdapter context = (DirContextAdapter)ctx;
				return context.getStringAttribute(applicationLdapProperties.getAttribute());
			}
		});
    	
    	return result != null;
    }
    
    private String getUsername(String username) {
    	if(applicationLdapProperties.getDomain() == null) {
    		return username;
    	}else {
    		return username + "@" + applicationLdapProperties.getDomain();
    	}
    }
    
}
