package com.dbs.loyalty.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.security.rest.RestAuthentication;
import com.dbs.loyalty.security.rest.RestAuthenticationProvider;
import com.dbs.loyalty.security.rest.RestTokenProvider;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.JWTLoginDto;
import com.dbs.loyalty.service.dto.JWTTokenDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JWTAuthenticationService {

	private final RestTokenProvider restTokenProvider;

    private final RestAuthenticationProvider restAuthenticationProvider;
    
	private final CustomerMapper customerMapper;
	
	public JWTTokenDto authenticate(JWTLoginDto jwtLoginDto) {
		RestAuthentication restAuthentication = restAuthenticationProvider.authenticate(new RestAuthentication(jwtLoginDto));
        SecurityContextHolder.getContext().setAuthentication(restAuthentication);
        boolean rememberMe = (jwtLoginDto.getRememberMe() == null) ? false : jwtLoginDto.getRememberMe();
        
        String token = restTokenProvider.createToken(restAuthentication, rememberMe);
        CustomerDto customerDto = customerMapper.toDto(restAuthentication.getCustomer());
        return new JWTTokenDto(token, customerDto);
	}
	
}
