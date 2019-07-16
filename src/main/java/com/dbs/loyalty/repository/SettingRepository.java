package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.mst.Setting;


public interface SettingRepository extends JpaRepository<Setting, String>{
	
	Optional<Setting> findByNameIgnoreCase(String name);
	
}
