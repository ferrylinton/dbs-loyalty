package com.dbs.priviledge.security;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.domain.User;
import com.dbs.priviledge.service.UserService;
import com.dbs.priviledge.util.PasswordUtil;

@Component("apiAuthenticationProvider")
public class ApiAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;
 
	public ApiAuthenticationProvider(UserService userService) {
		this.userService = userService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) {
		System.out.println("---------------- ApiAuthenticationProvider()");
		System.out.println("---------------- " + authentication.getCredentials().toString());
		System.out.println("---------------- " + authentication.getName().toLowerCase());
        String password = authentication.getCredentials().toString();
        String email = authentication.getName().toLowerCase();
       
        return authenticateFromDb(email, password);
	}

	private ApiAuthentication authenticateFromDb(String email, String password){
		Optional<User> user = userService.findByEmail(email);
		
		if(user.isPresent() && PasswordUtil.getInstance().matches(password, user.get().getPasswordHash())) {
			return new ApiAuthentication(email, password, user.get());
        }else{
        	throw new BadCredentialsException(Constant.EMPTY);
        }
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(ApiAuthentication.class);
	}
	
}