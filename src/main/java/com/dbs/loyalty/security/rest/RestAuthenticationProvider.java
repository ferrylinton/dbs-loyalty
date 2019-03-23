package com.dbs.loyalty.security.rest;

import static com.dbs.loyalty.config.Constant.EMPTY;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.util.PasswordUtil;

public class RestAuthenticationProvider implements AuthenticationProvider {

    private CustomerService customerService;
 
	public RestAuthenticationProvider(CustomerService customerService) {
		this.customerService = customerService;
	}

	@Override
	public RestAuthentication authenticate(Authentication authentication) {
		String email = authentication.getName().toLowerCase();
		String password = authentication.getCredentials().toString();
        
		Optional<Customer> customer = customerService.findByEmail(email);
		
		if(customer.isPresent() && PasswordUtil.getInstance().matches(password, customer.get().getPasswordHash())) {
			return new RestAuthentication(email, password, customer.get());
        }else{
        	throw new BadCredentialsException(EMPTY);
        }
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(RestAuthentication.class);
	}
	
}