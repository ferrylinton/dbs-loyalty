package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.AUTHENTICATE;
import static com.dbs.loyalty.config.constant.SwaggerConstant.AUTHENTICATION;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

/**
 * REST controller for Authenticate API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { AUTHENTICATION })
@RequiredArgsConstructor
@RestController
public class JWTRestController {

    private final JWTAuthenticationService jwtAuthenticationService;

    @ApiOperation(value=AUTHENTICATE, consumes=JSON, produces=JSON)
    @ApiNotes("authenticate.md")
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response=JWTTokenDto.class)})
    @PostMapping("/api/authenticate")
    public JWTTokenDto authenticate(
    		@ApiParam(name = "JWTLoginData", value = "Customer's login data to get JWT token")
    		@Valid @RequestBody JWTLoginDto jwtLoginDto) throws IOException {
    	
    	Authentication authentication = new UsernamePasswordAuthenticationToken(jwtLoginDto.getEmail(), jwtLoginDto.getPassword());
    	return jwtAuthenticationService.authenticate(authentication, jwtLoginDto.isRememberMe());
    }

}
