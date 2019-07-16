package com.dbs.loyalty.service;

import static com.dbs.loyalty.config.constant.MessageConstant.EXPIRED_TOKEN;
import static com.dbs.loyalty.config.constant.MessageConstant.INVALID_TOKEN;
import static com.dbs.loyalty.config.constant.MessageConstant.NOT_FOUND_TOKEN;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.domain.cst.Customer;
import com.dbs.loyalty.domain.sec.VerificationToken;
import com.dbs.loyalty.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VerificationTokenService {
	
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
