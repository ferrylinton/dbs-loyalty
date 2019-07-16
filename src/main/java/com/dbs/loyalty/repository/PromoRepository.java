package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dbs.loyalty.domain.prm.Promo;

public interface PromoRepository extends JpaRepository<Promo, String>, JpaSpecificationExecutor<Promo>{

	@Query(value = "select p.termAndCondition from Promo p where id= ?1")
	Optional<String> findTermAndConditionById(String id);
	
	Optional<Promo> findByCodeIgnoreCase(String code);
	
	Optional<Promo> findByTitleIgnoreCase(String title);
	
	@Query(value = "select p from Promo p "
			+ "JOIN FETCH p.promoCategory c "
			+ "where p.startPeriod <= current_date() and p.endPeriod >= current_date() "
			+ "and p.activated = true "
			+ "and c.id=:promoCategoryId")
	List<Promo> findByPromoCategoryId(@Param("promoCategoryId") String promoCategoryId);
	
	@Query(value = "from Promo p "
			+ "where p.startPeriod <= current_date() and p.endPeriod >= current_date() "
			+ "and p.activated = true and p.showInCarousel = true ")
	List<Promo> findPromoInCarousel();
	
	@Modifying
	@Query("update Promo p set p.pending = ?1 where p.id = ?2")
	void save(boolean pending, String id);
	
}
