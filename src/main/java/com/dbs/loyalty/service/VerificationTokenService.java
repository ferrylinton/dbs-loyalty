package com.dbs.loyalty.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.VerificationToken;
import com.dbs.loyalty.repository.VerificationTokenRepository;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VerificationTokenService {

	private final VerificationTokenRepository tokenRepository;
	
	public VerificationToken generate() {
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setEmail(SecurityUtil.getLogged());
		verificationToken.setToken(RandomUtils.nextInt(100000, 999999));
		verificationToken.setExpiryDate(Instant.now().plus(15, ChronoUnit.MINUTES));
		verificationToken.setCreatedDate(Instant.now());
		return tokenRepository.save(verificationToken);
	}
	
	public boolean verify(int token) {
		Optional<VerificationToken> verificationToken = tokenRepository.findByEmailAndToken(SecurityUtil.getLogged(), token);
		
		if(verificationToken.isPresent()) {
			if(!verificationToken.get().getToken().equals(token)) {
				return false;
			}else if(verificationToken.get().getExpiryDate().isBefore(Instant.now())) {
				return false;
			}
			
			return true;
		}else {
			return false;
		}
	}
	
}
