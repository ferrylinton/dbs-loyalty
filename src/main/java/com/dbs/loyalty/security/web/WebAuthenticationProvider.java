package com.dbs.loyalty.security.web;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.service.LdapService;
import com.dbs.loyalty.service.UserService;
 
public class WebAuthenticationProvider implements AuthenticationProvider {

	private final Logger LOG = LoggerFactory.getLogger(WebAuthenticationProvider.class);
			
    private UserService userService;
    
    private LdapService ldapService;
 
	public WebAuthenticationProvider(UserService userService, LdapService ldapService) {
		this.userService = userService;
		this.ldapService = ldapService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) {
		String password = authentication.getCredentials().toString();
        String username = authentication.getName();
        
        Optional<User> user = userService.findWithRoleByUsername(username);
        
        if(user.isPresent() && user.get().getAuthenticateFromDb()) {
        	LOG.info(String.format("[ %s ] login with DATABASE username/password", username));
        	return userService.authenticate(user.get(), password);
        }else if(user.isPresent() && !user.get().getAuthenticateFromDb()) {
        	LOG.info(String.format("[ %s ] login with LDAP username/password and ROLE from DATABASE", username));
        	return ldapService.authenticate(username, password, user.get().getRole());
        }else {
        	LOG.info(String.format("[ %s ] login with LDAP username/password and with DEFAULT ROLE", username));
        	return ldapService.authenticate(username, password, null);
        }
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}