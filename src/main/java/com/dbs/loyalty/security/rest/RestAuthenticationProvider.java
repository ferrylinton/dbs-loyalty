package com.dbs.loyalty.security.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.LOGIN_FAILED;
import static com.dbs.loyalty.config.constant.StatusConstant.FAIL;
import static com.dbs.loyalty.config.constant.StatusConstant.SUCCEED;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.LogToken;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.LogTokenService;
import com.dbs.loyalty.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository;
    
    private final LogTokenService logCustomerLoginService;
 
	@Override
	public RestAuthentication authenticate(Authentication authentication) {
		String email = authentication.getName().toLowerCase();
		String password = authentication.getCredentials().toString();
        
		Optional<Customer> customer = customerRepository.findByEmail(email);
		
		if(customer.isPresent() && PasswordUtil.matches(password, customer.get().getPasswordHash())) {
			save(email, SUCCEED);
			return new RestAuthentication(email, password, customer.get());
        }else{
        	save(email, FAIL);
        	throw new BadCredentialsException(LOGIN_FAILED);
        }
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(RestAuthentication.class);
	}
	
	private void save(String email, String status) {
		LogToken logCustomerLogin = new LogToken();
		logCustomerLogin.setEmail(email);
		logCustomerLogin.setStatus(status);
		logCustomerLogin.setCreatedDate(Instant.now());
		logCustomerLoginService.save(logCustomerLogin);
	}
	
}