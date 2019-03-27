package com.dbs.loyalty.web.controller.rest;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.service.JWTAuthenticationService;
import com.dbs.loyalty.service.dto.JWTLoginDto;
import com.dbs.loyalty.service.dto.JWTTokenDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller to authenticate users.
 */
@Api(tags = { SwaggerConstant.Authentication })
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api")
public class JWTRestController {

    private final JWTAuthenticationService jwtAuthenticationService;

    @PostMapping("/authenticate")
    @ApiOperation(nickname = "authenticate", value="authenticate", notes="Authenticate customer to access DBS API", produces=MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response = JWTTokenDto.class)})
    public ResponseEntity<?> authenticate(
    		@ApiParam(name = "JWTLoginDto", value = "Customer's login data to get access token") 
    		@Valid @RequestBody JWTLoginDto jwtLoginDto) {
    	
    	try {
    		JWTTokenDto jwtTokenDto = jwtAuthenticationService.authenticate(jwtLoginDto);
            return new ResponseEntity<>(jwtTokenDto, HttpStatus.OK);
		} catch (BadCredentialsException e) {
			log.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(new ErrorResponse("Wrong Email or Password"), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return new ResponseEntity<>(new ErrorResponse(e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    	
    }

}
