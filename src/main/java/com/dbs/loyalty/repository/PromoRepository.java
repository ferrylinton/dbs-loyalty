package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbs.loyalty.domain.Promo;

public interface PromoRepository extends JpaRepository<Promo, String>, JpaSpecificationExecutor<Promo>{

	Optional<Promo> findByCodeIgnoreCase(String code);
	
	Optional<Promo> findByTitleIgnoreCase(String title);
	
	@Query(value = "from Promo p "
			+ "JOIN FETCH p.promoCategory c "
			+ "where p.startPeriod <= current_date() and p.endPeriod >= current_date() "
			+ "and p.activated = true "
			+ "and c.id=:promoCategoryId")
	List<Promo> findByPromoCategoryId(@Param("promoCategoryId") String promoCategoryId);
	
	@Query(value = "from Promo p "
			+ "where p.startPeriod <= current_date() and p.endPeriod >= current_date() "
			+ "and p.activated = true and p.showInCarousel = true ")
	List<Promo> findPromoInCarousel();
	
}
