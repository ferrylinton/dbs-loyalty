package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.PromoCategory;

public interface PromoCategoryRepository extends JpaRepository<PromoCategory, String>, JpaSpecificationExecutor<PromoCategory>{

	Page<PromoCategory> findAllByNameContainingAllIgnoreCase(String name, Pageable pageable);
	
	Optional<PromoCategory> findByNameIgnoreCase(String name);
	
	@Modifying
	@Query("update PromoCategory p set p.pending = ?1 where p.id = ?2")
	void save(boolean pending, String id);
	
}
