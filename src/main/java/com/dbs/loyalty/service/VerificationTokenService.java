package com.dbs.loyalty.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.VerificationToken;
import com.dbs.loyalty.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VerificationTokenService {
	
	public static final String INVALID_TOKEN = "Token is invalid";
	
	public static final String EXPIRED_TOKEN = "Token is expired";
	
	public static final String NOT_FOUND_TOKEN = "Token is not found";
	
	private final VerificationTokenRepository tokenRepository;
	
	private final CustomerService customerService;
	
	private final ApplicationProperties applicationProperties;
	
	public VerificationToken generate(String email) {
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setEmail(email);
		verificationToken.setToken(RandomUtils.nextInt(100000, 999999));
		verificationToken.setExpiryDate(Instant.now().plus(applicationProperties.getSecurity().getVerificationTokenValidity(), ChronoUnit.MINUTES));
		verificationToken.setCreatedDate(Instant.now());
		return tokenRepository.save(verificationToken);
	}
	
	public VerificationToken verify(String email, int token) {
		Optional<VerificationToken> verificationToken = tokenRepository.findByEmailAndToken(email, token);
		
		if(verificationToken.isPresent()) {
			if(!verificationToken.get().getToken().equals(token)) {
				throw new BadCredentialsException(INVALID_TOKEN);
			}else if(verificationToken.get().getExpiryDate().isBefore(Instant.now())) {
				throw new BadCredentialsException(EXPIRED_TOKEN);
			}
			
			Optional<Customer> customer = customerService.findByEmail(email);
			if(customer.isPresent()) {
				verificationToken.get().setCustomer(customer.get());
			}
			
			return verificationToken.get();
		}else {
			throw new BadCredentialsException(NOT_FOUND_TOKEN);
		}
	}
	
}
