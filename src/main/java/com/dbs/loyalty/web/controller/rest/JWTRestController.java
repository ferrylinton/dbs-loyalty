package com.dbs.loyalty.web.controller.rest;

import javax.validation.Valid;

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
import com.dbs.loyalty.web.swagger.ApiNotes;

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
    @ApiOperation(nickname = "authenticate", value="authenticate", produces=MediaType.APPLICATION_JSON_VALUE)
    @ApiNotes("authenticate.md")
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = JWTTokenDto.class)})
    public ResponseEntity<?> authenticate(
    		@ApiParam(name = "JWTLoginData", value = "Customer's login data to get access token") 
    		@Valid @RequestBody JWTLoginDto jwtLoginDto) {
    	
    	try {
    		JWTTokenDto jwtTokenDto = jwtAuthenticationService.authenticate(jwtLoginDto);
    		return ResponseEntity.ok(jwtTokenDto);
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(401).body(new ErrorResponse("Wrong Email or Password"));
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
			return ResponseEntity.status(500).body(new ErrorResponse(e.getLocalizedMessage()));
		}
    	
    }

}
