package com.dbs.loyalty.web.controller.rest;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.model.Login;
import com.dbs.loyalty.security.rest.RestAuthentication;
import com.dbs.loyalty.security.rest.RestAuthenticationProvider;
import com.dbs.loyalty.security.rest.RestTokenProvider;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final RestTokenProvider restTokenProvider;

    private final RestAuthenticationProvider restAuthenticationProvider;

    public UserJWTController(RestTokenProvider restTokenProvider, RestAuthenticationProvider restAuthenticationProvider) {
        this.restTokenProvider = restTokenProvider;
        this.restAuthenticationProvider = restAuthenticationProvider;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody Login login) {
    	RestAuthentication authentication = restAuthenticationProvider.authenticate(new RestAuthentication(login.getEmail(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (login.getRememberMe() == null) ? false : login.getRememberMe();
        String token = restTokenProvider.createToken(authentication, rememberMe);
        return new ResponseEntity<>(new JWTToken(token, authentication.getCustomer()), HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String token;
        
        private Customer customer;

		public JWTToken(String token, Customer customer) {
			this.token = token;
			this.customer = customer;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public Customer getCustomer() {
			return customer;
		}

		public void setCustomer(Customer customer) {
			this.customer = customer;
		}

    }
}
