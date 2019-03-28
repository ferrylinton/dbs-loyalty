package com.dbs.loyalty.security.web;

import static com.dbs.loyalty.config.constant.Constant.EMPTY;

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

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.domain.enumeration.UserType;
import com.dbs.loyalty.ldap.service.AuthenticateLdapService;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.util.PasswordUtil;

import lombok.RequiredArgsConstructor;
 
@RequiredArgsConstructor
public class WebAuthenticationProvider implements AuthenticationProvider {
		
    private final UserRepository userRepository;
    
    private final AuthenticateLdapService authenticateLdapService;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String password = authentication.getCredentials().toString();
        String username = authentication.getName();
        
        Optional<User> user = userRepository.findWithRoleByUsername(username);
        
        if(user.isPresent()) {
        	if(!user.get().isActivated()) {
        		throw new DisabledException(EMPTY);
            }else if(user.get().isLocked()) {
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
    		return new UsernamePasswordAuthenticationToken(user.getUsername(), EMPTY, getAuthorities(user));
		}else {
			addLoginAttemptCount(user.getLoginAttemptCount(), user.getUsername());
        	throw new BadCredentialsException(EMPTY);
        }
	}
	
	private boolean isAuthenticated(String password, User user) {
		if(user.getUserType() == UserType.Internal) {
			return authenticateLdapService.authenticate(user.getUsername(), password);
		}else{
			return PasswordUtil.getInstance().matches(password, user.getPasswordHash());
        }
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		for(Authority authority : user.getRole().getAuthorities()){
			authorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
		
        return authorities;
    }
	
	public void addLoginAttemptCount(Integer loginAttemptCount, String username) {
		loginAttemptCount = (loginAttemptCount < 3) ? (loginAttemptCount + 1) : loginAttemptCount;
		
		if(loginAttemptCount == 3) {
			userRepository.lockUser(loginAttemptCount, true, username);
		}else {
			userRepository.updateLoginAttemptCount(loginAttemptCount, username);
		}
		
	}

	public void resetLoginAttemptCount(String username) {
		userRepository.updateLoginAttemptCount(0, username);
	}
	
}