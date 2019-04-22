package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.FilePdf;

public interface FilePdfRepository extends JpaRepository<FilePdf, String>{

	@Query(value = "select p.* from f_pdf p "
			+ "left join c_event e on p.id = e.id "
			+ "where e.id = ?1", 
			  nativeQuery = true)
	Optional<FilePdf> findOneByEventId(String id);
	
}
