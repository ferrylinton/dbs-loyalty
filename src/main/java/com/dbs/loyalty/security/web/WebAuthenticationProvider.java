package com.dbs.loyalty.security.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.ldap.LdapService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.util.PasswordUtil;
 
public class WebAuthenticationProvider implements AuthenticationProvider {
		
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
        
        if(user.isPresent()) {
        	if(!user.get().isActivated()) {
        		throw new DisabledException(Constant.EMPTY);
            }else if(user.get().isLocked()) {
        		throw new LockedException(Constant.EMPTY);
        	}else {
        		return authenticate(password, user.get());
        	}
        }else {
        	throw new BadCredentialsException(Constant.EMPTY);
        }
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	private Authentication authenticate(String password, User user) {
		boolean authenticated = false;
		
		System.out.println("user.getAuthenticateFromDb() : " + user.getAuthenticateFromDb());
		if(user.getAuthenticateFromDb()) {
			authenticated = PasswordUtil.getInstance().matches(password, user.getPasswordHash());
		}else{
			authenticated = ldapService.authenticate(user.getUsername(), password);
        }
		
		if(authenticated) {
			userService.resetLoginAttemptCount(user.getUsername());
    		return new UsernamePasswordAuthenticationToken(user.getUsername(), Constant.EMPTY, getAuthorities(user));
		}else {
			userService.addLoginAttemptCount(user.getLoginAttemptCount(), user.getUsername());
        	throw new BadCredentialsException(Constant.EMPTY);
        }
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		for(Authority authority : user.getRole().getAuthorities()){
			authorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
		
        return authorities;
    }
	
}