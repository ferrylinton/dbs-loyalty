package com.dbs.loyalty.web.controller.rest;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.TagConstant;
import com.dbs.loyalty.model.JWTToken;
import com.dbs.loyalty.model.Login;
import com.dbs.loyalty.security.rest.RestAuthentication;
import com.dbs.loyalty.security.rest.RestAuthenticationProvider;
import com.dbs.loyalty.security.rest.RestTokenProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller to authenticate users.
 */
@Api(tags = { TagConstant.Authentication })
@RestController
@RequestMapping("/api")
public class JWTRestController {

    private final RestTokenProvider restTokenProvider;

    private final RestAuthenticationProvider provider;

    public JWTRestController(RestTokenProvider restTokenProvider, RestAuthenticationProvider restAuthenticationProvider) {
        this.restTokenProvider = restTokenProvider;
        this.provider = restAuthenticationProvider;
    }

    @ApiOperation(
    	nickname = "authenticate",
    	notes="${JWTController.authenticate.notes}", 
    	value="${JWTController.authenticate.value}", 
    	response = JWTToken.class
    )
    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authenticate(@Valid @RequestBody Login login) {
    	RestAuthentication authentication = provider.authenticate(new RestAuthentication(login.getEmail(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (login.getRememberMe() == null) ? false : login.getRememberMe();
        String token = restTokenProvider.createToken(authentication, rememberMe);
        return new ResponseEntity<>(new JWTToken(token, authentication.getCustomer()), HttpStatus.OK);
    }

}
