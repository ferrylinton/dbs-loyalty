package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.med.Wellness;

public interface WellnessRepository extends JpaRepository<Wellness, String>{
	
}
