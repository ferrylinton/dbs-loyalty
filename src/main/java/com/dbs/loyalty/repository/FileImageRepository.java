package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.FileImage;

public interface FileImageRepository extends JpaRepository<FileImage, String>{

	@Query(value = "select i.* from f_image i "
			+ "left join c_customer c on i.id = c.id "
			+ "where c.email = ?1", 
			  nativeQuery = true)
	Optional<FileImage> findOneByCustomerEmail(String email);
	
	@Query(value = "select i.* from f_image i "
			+ "left join c_promo p on i.id = p.id "
			+ "where p.id = ?1", 
			  nativeQuery = true)
	Optional<FileImage> findOneByPromoId(String id);
	
	@Query(value = "select i.* from f_image i "
			+ "left join c_event e on i.id = e.id "
			+ "where e.id = ?1", 
			  nativeQuery = true)
	Optional<FileImage> findOneByEventId(String id);
	
}
