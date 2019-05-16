package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.LogConstant.GENERATE_VERIFICATION_TOKEN;
import static com.dbs.loyalty.config.constant.LogConstant.VERIFY_VERIFICATION_TOKEN;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.VERIFICATION_TOKEN;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.VerificationToken;
import com.dbs.loyalty.service.MailService;
import com.dbs.loyalty.service.VerificationTokenService;
import com.dbs.loyalty.service.dto.VerificationTokenDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api")
public class VerificationTokenRestController {
	
	private static final String VERIFIED = "verified";

    private final VerificationTokenService verificationTokenService;
    
    private final MailService mailService;

    /**
     * GET  /api/verification-tokens/generate : generate token
     *
     * @return token
     */
    @ApiOperation(nickname=GENERATE_VERIFICATION_TOKEN, value=GENERATE_VERIFICATION_TOKEN, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=OK, response=VerificationTokenDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/verification-tokens/generate")
    public VerificationTokenDto generate() {
    	VerificationToken verificationToken = verificationTokenService.generate();
    	mailService.sendToken(verificationToken.getEmail(), verificationToken.getToken());
    	return new VerificationTokenDto(verificationToken.getToken());
    }
    
    /**
     * POST  /api/verification-tokens/verify : generate token
     *
     * @return boolean
     */
    @ApiOperation(nickname=VERIFY_VERIFICATION_TOKEN, value=VERIFY_VERIFICATION_TOKEN, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value = { @ApiResponse(code=200, message=OK, response=VerificationTokenDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/verification-tokens/verify")
    public Map<String, Boolean> verify(@Valid @RequestBody VerificationTokenDto verificationToken) {
    	boolean verified = verificationTokenService.verify(verificationToken.getToken());
    	return Collections.singletonMap(VERIFIED, verified);
    }

}
