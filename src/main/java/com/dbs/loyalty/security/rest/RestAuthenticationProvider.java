package com.dbs.loyalty.security.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.CUSTOMER_LOCKED;
import static com.dbs.loyalty.config.constant.MessageConstant.CUSTOMER_NOT_ACTIVATED;
import static com.dbs.loyalty.config.constant.MessageConstant.LOGIN_FAILED;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.dbs.loyalty.config.constant.SecurityConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.model.TokenData;
import com.dbs.loyalty.repository.CustomerRepository;
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
		
		if(customer.isPresent()) {
			if(!customer.get().getActivated()) {
				throw new BadCredentialsException(CUSTOMER_NOT_ACTIVATED);
			}else if(customer.get().getLocked()) {
				throw new BadCredentialsException(CUSTOMER_LOCKED);
			}else if(PasswordUtil.matches(password, customer.get().getPasswordHash())) {
				TokenData tokenData = new TokenData();
				tokenData.setId(customer.get().getId());
				tokenData.setEmail(email);
				tokenData.setRole(SecurityConstant.ROLE_CUSTOMER);
				return new RestAuthentication(tokenData, customer.get());
			}
        }
		
		throw new BadCredentialsException(LOGIN_FAILED);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(RestAuthentication.class);
	}
	
}