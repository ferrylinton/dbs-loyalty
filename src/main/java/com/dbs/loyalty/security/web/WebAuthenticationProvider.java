package com.dbs.loyalty.security.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.service.LdapService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.util.PasswordUtil;
 
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
        	LOG.info("db authentication");
        	if(PasswordUtil.getInstance().matches(password, user.get().getPasswordHash())) {
    			return new UsernamePasswordAuthenticationToken(username, password, getAuthorities(user.get()));
            }
        }else if(user.isPresent() && !user.get().getAuthenticateFromDb()) {
        	LOG.info("ldap authentication and role from db");
        	return ldapService.authenticate(username, password, user.get().getRole());
        }else {
        	LOG.info("ldap authentication with default role");
        	return ldapService.authenticate(username, password, null);
        }
		
        throw new BadCredentialsException(Constant.EMPTY);
	}
 
	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		for(Authority authority : user.getRole().getAuthorities()){
			authorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
		
        return authorities;
    }
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}