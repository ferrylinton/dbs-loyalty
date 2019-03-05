package com.dbs.loyalty.security.rest;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.util.PasswordUtil;

public class RestAuthenticationProvider implements AuthenticationProvider {

    private CustomerService customerService;
 
	public RestAuthenticationProvider(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) {
        String password = authentication.getCredentials().toString();
        String email = authentication.getName().toLowerCase();
       
        return authenticateFromDb(email, password);
	}

	private RestAuthentication authenticateFromDb(String email, String password){
		Optional<Customer> customer = customerService.findByEmail(email);
		
		if(customer.isPresent() && PasswordUtil.getInstance().matches(password, customer.get().getPasswordHash())) {
			return new RestAuthentication(email, password, customer.get());
        }else{
        	throw new BadCredentialsException(Constant.EMPTY);
        }
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(RestAuthentication.class);
	}
	
}