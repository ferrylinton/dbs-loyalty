package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.GENERATE_VERIFICATION_TOKEN;
import static com.dbs.loyalty.config.constant.RestConstant.LOGIN_WITH_TOKEN;
import static com.dbs.loyalty.config.constant.RestConstant.VERIFY_VERIFICATION_TOKEN;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.VERIFICATION_TOKEN;
import static com.dbs.loyalty.config.constant.SecurityConstant.ROLE_TOKEN;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.ApplicationProperties;

import com.dbs.loyalty.domain.VerificationToken;
import com.dbs.loyalty.model.TokenData;
import com.dbs.loyalty.security.rest.RestTokenProvider;
import com.dbs.loyalty.service.MailService;
import com.dbs.loyalty.service.VerificationTokenService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.service.dto.GenerateTokenDto;
import com.dbs.loyalty.service.dto.JWTTokenDto;
import com.dbs.loyalty.service.dto.VerificationTokenDto;
import com.dbs.loyalty.service.mapper.CustomerMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for VerificationToken API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Api(tags = { VERIFICATION_TOKEN })
@RequiredArgsConstructor
@RestController
public class VerificationTokenRestController {
	
	private static final String VERIFIED = "verified";

    private final VerificationTokenService verificationTokenService;
    
    private final MailService mailService;
    
    private final RestTokenProvider restTokenProvider;
    
    private final CustomerMapper customerMapper;

    private final ApplicationProperties applicationProperties;
    
    /**
     * POST  /api/verification-tokens/generate : generate token
     *
     * @return token
     */
    @ApiOperation(nickname=GENERATE_VERIFICATION_TOKEN, value=GENERATE_VERIFICATION_TOKEN, consumes=JSON, produces=JSON)
    @ApiResponses(value = { @ApiResponse(code=200, message=OK, response=VerificationTokenDto.class)})
    @PostMapping("/api/verification-tokens/generate")
    public VerificationTokenDto generate(
    		@ApiParam(name = "GenerateTokenData", value = "Generate token data")
    		@Valid @RequestBody GenerateTokenDto generateToken) {
    	
    	VerificationToken verificationToken = verificationTokenService.generate(generateToken.getEmail());
    	mailService.sendToken(verificationToken.getEmail(), verificationToken.getToken());
    	
    	VerificationTokenDto verificationDto = new VerificationTokenDto();
    	verificationDto.setEmail(generateToken.getEmail());
    	return verificationDto;
    }
    
    /**
     * POST  /api/verification-tokens/verify : generate token
     *
     * @return boolean
     */
    @ApiOperation(nickname=VERIFY_VERIFICATION_TOKEN, value=VERIFY_VERIFICATION_TOKEN, consumes=JSON, produces=JSON)
    @ApiResponses(value = { @ApiResponse(code=200, message=OK, response=VerificationTokenDto.class)})
    @PostMapping("/api/verification-tokens/verify")
    public Map<String, Boolean> verify(
    		@ApiParam(name = "VerificationTokenData", value = "Verification token data")
    		@Valid @RequestBody VerificationTokenDto verificationTokenDto) {
    	
    	VerificationToken verificationToken = verificationTokenService.verify(verificationTokenDto.getEmail(), verificationTokenDto.getToken());
    	return Collections.singletonMap(VERIFIED, verificationToken != null);
    }
    
    /**
     * POST  /api/verification-tokens/authenticate : authenticate
     *
     * @return JWTTokenDto
     * @throws IOException 
     */
    @ApiOperation(nickname=LOGIN_WITH_TOKEN, value=LOGIN_WITH_TOKEN, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=OK, response=JWTTokenDto.class)})
    @PostMapping("/api/verification-tokens/authenticate")
    public JWTTokenDto authenticate(
    		@ApiParam(name = "VerificationTokenData", value = "Verification token data")
    		@Valid @RequestBody VerificationTokenDto verificationTokenDto) throws IOException {
    	
    	VerificationToken verificationToken = verificationTokenService.verify(verificationTokenDto.getEmail(), verificationTokenDto.getToken());
    	String token = restTokenProvider.createToken(getTokenData(verificationToken), getValidity());
    	CustomerDto customerDto = customerMapper.toDto(verificationToken.getCustomer());
        return new JWTTokenDto(token, customerDto);
    }

    private TokenData getTokenData(VerificationToken verificationToken) {
    	TokenData tokenData = new TokenData();
    	tokenData.setId(verificationToken.getCustomer().getId());
    	tokenData.setEmail(verificationToken.getCustomer().getEmail());
    	tokenData.setRole(ROLE_TOKEN);
    	return tokenData;
    }
    
    private Date getValidity() {
    	long now = (new Date()).getTime();
    	return new Date(now + (applicationProperties.getSecurity().getVerificationTokenValidity() * 60 * 1000));
    }
    
}
