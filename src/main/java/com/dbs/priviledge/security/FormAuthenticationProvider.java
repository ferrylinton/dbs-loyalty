package com.dbs.priviledge.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.domain.Authority;
import com.dbs.priviledge.domain.User;
import com.dbs.priviledge.service.UserService;
import com.dbs.priviledge.util.PasswordUtil;
 
public class FormAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
 
	public FormAuthenticationProvider(UserService userService) {
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) {
		String password = authentication.getCredentials().toString();
        String email = authentication.getName().toLowerCase();
       
        return authenticateFromDb(email, password);
	}

	private Authentication authenticateFromDb(String email, String password){
		Optional<User> user = userService.findByEmail(email);
		
		if(user.isPresent() && PasswordUtil.getInstance().matches(password, user.get().getPasswordHash())) {
			return new UsernamePasswordAuthenticationToken(email, password, getAuthorities(user.get()));
        }else{
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
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}