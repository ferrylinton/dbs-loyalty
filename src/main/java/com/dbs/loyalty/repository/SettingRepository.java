package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.Setting;


public interface SettingRepository extends JpaRepository<Setting, String>, JpaSpecificationExecutor<Setting>{
	
	Optional<Setting> findByNameIgnoreCase(String name);
	
}
