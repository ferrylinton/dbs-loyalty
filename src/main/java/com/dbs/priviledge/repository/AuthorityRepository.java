package com.dbs.priviledge.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.priviledge.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, String>{

	Page<Authority> findAllByNameContainingAllIgnoreCase(String name, Pageable pageable);
	
	Optional<Authority> findByNameIgnoreCase(String name);
	
}
