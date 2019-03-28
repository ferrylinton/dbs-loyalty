package com.dbs.loyalty.ldap.service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import javax.naming.directory.DirContext;

import org.springframework.ldap.core.AuthenticatedLdapEntryContextMapper;
import org.springframework.ldap.core.LdapEntryIdentification;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.AuthenticationLdapProperties;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticateLdapService {
	
	private final LdapTemplate ldapTemplate;

    private final AuthenticationLdapProperties applicationLdapProperties;
    
    public boolean authenticate(String username, String password) {
    	return ldapTemplate.authenticate(query().where(applicationLdapProperties.getAttribute()).is(username), password, new AuthenticatedLdapEntryContextMapper<Boolean>() {

			@Override
			public Boolean mapWithContext(DirContext ctx, LdapEntryIdentification ldapEntryIdentification) {
				return true;
			}
			
		});
    }
    
}
