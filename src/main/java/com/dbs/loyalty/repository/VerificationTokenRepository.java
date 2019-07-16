package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String>{
	
	Optional<VerificationToken> findByEmailAndToken(String email, Integer token);
	
}
