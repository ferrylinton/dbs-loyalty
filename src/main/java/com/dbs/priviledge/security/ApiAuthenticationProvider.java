package com.dbs.priviledge.security;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.domain.Customer;
import com.dbs.priviledge.service.CustomerService;
import com.dbs.priviledge.util.PasswordUtil;

public class ApiAuthenticationProvider implements AuthenticationProvider {

    private CustomerService customerService;
 
	public ApiAuthenticationProvider(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) {
        String password = authentication.getCredentials().toString();
        String email = authentication.getName().toLowerCase();
       
        return authenticateFromDb(email, password);
	}

	private ApiAuthentication authenticateFromDb(String email, String password){
		Optional<Customer> customer = customerService.findByEmail(email);
		
		if(customer.isPresent() && PasswordUtil.getInstance().matches(password, customer.get().getPasswordHash())) {
			return new ApiAuthentication(email, password, customer.get());
        }else{
        	throw new BadCredentialsException(Constant.EMPTY);
        }
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(ApiAuthentication.class);
	}
	
}