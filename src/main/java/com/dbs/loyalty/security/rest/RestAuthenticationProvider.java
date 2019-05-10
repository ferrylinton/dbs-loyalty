package com.dbs.loyalty.security.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.LOGIN_FAILED;

import java.time.Instant;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.LogCustomerLogin;
import com.dbs.loyalty.domain.enumeration.LoginStatus;
import com.dbs.loyalty.repository.CustomerRepository;
import com.dbs.loyalty.service.LogCustomerLoginService;
import com.dbs.loyalty.util.PasswordUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

    private final CustomerRepository customerRepository;
    
    private final LogCustomerLoginService logCustomerLoginService;
 
	@Override
	public RestAuthentication authenticate(Authentication authentication) {
		String email = authentication.getName().toLowerCase();
		String password = authentication.getCredentials().toString();
        
		Optional<Customer> customer = customerRepository.findByEmail(email);
		
		if(customer.isPresent() && PasswordUtil.matches(password, customer.get().getPasswordHash())) {
			save(email, LoginStatus.SUCCEED);
			return new RestAuthentication(email, password, customer.get());
        }else{
        	save(email, LoginStatus.FAIL);
        	throw new BadCredentialsException(LOGIN_FAILED);
        }
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(RestAuthentication.class);
	}
	
	private void save(String email, LoginStatus loginStatus) {
		LogCustomerLogin logCustomerLogin = new LogCustomerLogin();
		logCustomerLogin.setEmail(email);
		logCustomerLogin.setLoginStatus(loginStatus);
		logCustomerLogin.setCreatedDate(Instant.now());
		logCustomerLoginService.save(logCustomerLogin);
	}
	
}