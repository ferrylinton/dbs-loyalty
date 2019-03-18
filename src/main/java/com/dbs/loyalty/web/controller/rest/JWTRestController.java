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
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.model.JWTToken;
import com.dbs.loyalty.model.Login;
import com.dbs.loyalty.security.rest.RestAuthentication;
import com.dbs.loyalty.security.rest.RestAuthenticationProvider;
import com.dbs.loyalty.security.rest.RestTokenProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

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

    @PostMapping("/authenticate")
    @ApiOperation(
    	nickname = "authenticate",
    	notes="${JWTController.authenticate.notes}", 
    	value="${JWTController.authenticate.value}"
    )
	@ApiResponses(value={
			@ApiResponse(code=200, message="Ok", response = JWTToken.class),
			@ApiResponse(code=500, message="Internal Server Error", response = ErrorResponse.class),
			@ApiResponse(code=401, message="Unauthorize", response = ErrorResponse.class, 
							examples = @Example(
								value = @ExampleProperty(value = "{\"message\"： \"Wrong Email or Password\"}", mediaType = "application/json")
							))
    })
    public ResponseEntity<?> authenticate(@Valid @RequestBody Login login) {
    	try {
    		RestAuthentication authentication = provider.authenticate(new RestAuthentication(login.getEmail(), login.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (login.getRememberMe() == null) ? false : login.getRememberMe();
            String token = restTokenProvider.createToken(authentication, rememberMe);
            return new ResponseEntity<>(new JWTToken(token, authentication.getCustomer()), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ErrorResponse("Wrong Email or Password"), HttpStatus.UNAUTHORIZED);
		}
    	
    }

}
