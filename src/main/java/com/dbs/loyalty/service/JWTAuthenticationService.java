package com.dbs.loyalty.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.model.TokenData;
import com.dbs.loyalty.security.rest.RestAuthentication;
import com.dbs.loyalty.security.rest.RestAuthenticationProvider;
import com.dbs.loyalty.security.rest.RestTokenProvider;
import com.dbs.loyalty.service.dto.JWTTokenDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JWTAuthenticationService {

	private final RestTokenProvider restTokenProvider;

    private final RestAuthenticationProvider restAuthenticationProvider;
    
	private final CustomerMapper customerMapper;
	
	private final ApplicationProperties applicationProperties;
	
	public JWTTokenDto authenticate(HttpServletRequest request, Authentication authentication, boolean rememberMe) throws Exception {
		RestAuthentication restAuthentication = restAuthenticationProvider.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(restAuthentication);
	       
	 	String token = restTokenProvider.createToken(getTokenData(restAuthentication), getValidity(rememberMe));
	 	return new JWTTokenDto(token, customerMapper.toDto(restAuthentication.getCustomer()));
	}
	
	public TokenData getTokenData(RestAuthentication restAuthentication) {
		TokenData tokenData = new TokenData();
		tokenData.setId(restAuthentication.getId());
		tokenData.setEmail(restAuthentication.getName());
		tokenData.setRole(restAuthentication.getRole());
		return tokenData;
	}
	
	public Date getValidity(boolean rememberMe) {
		long now = (new Date()).getTime();
		long tokenValidityInMilliseconds = 0L;

		if (rememberMe) {
			tokenValidityInMilliseconds = applicationProperties.getSecurity().getTokenValidityForRememberMe() * 60 * 1000;
	    } else {
	    	tokenValidityInMilliseconds = applicationProperties.getSecurity().getTokenValidity() * 60 * 1000;
	    }
		
		return new Date(now + tokenValidityInMilliseconds);
	}
	
}
