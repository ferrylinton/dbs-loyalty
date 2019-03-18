package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.Promo;

public interface PromoRepository extends JpaRepository<Promo, String>, JpaSpecificationExecutor<Promo>{

	Optional<Promo> findByCodeIgnoreCase(String code);
	
	Optional<Promo> findByTitleIgnoreCase(String title);
	
	List<Promo> findByPromoCategoryId(String promoCategoryId);
	
}
