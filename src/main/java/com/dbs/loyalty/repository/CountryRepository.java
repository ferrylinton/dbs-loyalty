package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Country;

public interface CountryRepository extends JpaRepository<Country, Integer>{
	
	List<Country> findAll(Sort sort);
	
	@Query(value = "select c from Country c "
			+ "JOIN FETCH c.airports a "
			+ "order by c.name")
	List<Country> findWithAirports();
	
}
