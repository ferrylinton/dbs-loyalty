package com.dbs.loyalty.security.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.INVALID_EMAIL_OR_PASS;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository;
 
	@Override
	public RestAuthentication authenticate(Authentication authentication) {
		String email = authentication.getName().toLowerCase();
		String password = authentication.getCredentials().toString();
        
		Optional<Customer> customer = customerRepository.findByEmail(email);
		
		if(customer.isPresent() && PasswordUtil.getInstance().matches(password, customer.get().getPasswordHash())) {
			return new RestAuthentication(email, password, customer.get());
        }else{
        	throw new BadCredentialsException(MessageService.getMessage(INVALID_EMAIL_OR_PASS));
        }
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(RestAuthentication.class);
	}
	
}