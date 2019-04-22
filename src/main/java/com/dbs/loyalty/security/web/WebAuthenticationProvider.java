package com.dbs.loyalty.security.web;

import static com.dbs.loyalty.config.constant.Constant.EMPTY;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.domain.enumeration.UserType;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.service.AuthenticateLdapService;
import com.dbs.loyalty.util.PasswordUtil;

import lombok.RequiredArgsConstructor;
 
@RequiredArgsConstructor
public class WebAuthenticationProvider implements AuthenticationProvider {
		
    private final UserRepository userRepository;
    
    private final AuthenticateLdapService authenticateLdapService;
    
    private final ApplicationProperties applicationProperties;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String password = authentication.getCredentials().toString();
        String username = authentication.getName();
        
        Optional<User> user = userRepository.findWithRoleByUsername(username);
        
        if(user.isPresent()) {
        	if(!user.get().getActivated()) {
        		throw new DisabledException(EMPTY);
            }else if(user.get().getLocked()) {
        		throw new LockedException(EMPTY);
        	}else {
        		return authenticate(password, user.get());
        	}
        }else {
        	throw new BadCredentialsException(EMPTY);
        }
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	private Authentication authenticate(String password, User user) {
		if(isAuthenticated(password, user)) {
			resetLoginAttemptCount(user.getUsername());
			return new WebAuthentication(user);
		}else {
			addLoginAttemptCount(user.getLoginAttemptCount(), user.getUsername());
        	throw new BadCredentialsException(EMPTY);
        }
	}
	
	private boolean isAuthenticated(String password, User user) {
		if(user.getUserType() == UserType.INTERNAL) {
			return authenticateLdapService.authenticate(user.getUsername(), password);
		}else{
			return PasswordUtil.matches(password, user.getPasswordHash());
        }
	}
	
	public void addLoginAttemptCount(Integer loginAttemptCount, String username) {
		loginAttemptCount = (loginAttemptCount < getMaxAttempt()) ? (loginAttemptCount + 1) : loginAttemptCount;
		
		if(loginAttemptCount == 3) {
			userRepository.lockUser(loginAttemptCount, true, username);
		}else {
			userRepository.updateLoginAttemptCount(loginAttemptCount, username);
		}
		
	}

	public void resetLoginAttemptCount(String username) {
		userRepository.updateLoginAttemptCount(0, username);
	}
	
	private int getMaxAttempt() {
		return applicationProperties.getSecurity().getMaxAttempt();
	}
	
}