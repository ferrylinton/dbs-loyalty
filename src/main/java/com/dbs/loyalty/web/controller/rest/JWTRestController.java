package com.dbs.loyalty.web.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.service.JWTAuthenticationService;
import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.service.dto.JWTLoginDto;
import com.dbs.loyalty.service.dto.JWTTokenDto;
import com.dbs.loyalty.util.UrlUtil;
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
@Api(tags = { SwaggerConstant.AUTHENTICATION })
@RequiredArgsConstructor
@RestController
public class JWTRestController {

	public static final String AUTHENTICATE = "Authenticate";
	
	public static final String BINDER_NAME = "JWTLoginDto";
	
    private final JWTAuthenticationService jwtAuthenticationService;
    
    private final LogAuditCustomerService logAuditCustomerService;

    @ApiOperation(value=AUTHENTICATE, consumes="application/json", produces="application/json")
    @ApiNotes("authenticate.md")
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=JWTTokenDto.class)})
    @PostMapping("/api/authenticate")
    public JWTTokenDto authenticate(
    		@ApiParam(name = "JWTLoginData", value = "Customer's login data to get JWT token")
    		@Valid @RequestBody JWTLoginDto jwtLoginDto,
    		HttpServletRequest request) throws Exception {

    	Authentication authentication = new UsernamePasswordAuthenticationToken(jwtLoginDto.getEmail(), jwtLoginDto.getPassword());
		JWTTokenDto jWTTokenDto = jwtAuthenticationService.authenticate(request, authentication, jwtLoginDto.isRememberMe());
		logAuditCustomerService.save(AUTHENTICATE, UrlUtil.getFullUrl(request), jwtLoginDto);
		return jWTTokenDto;
    }

}
