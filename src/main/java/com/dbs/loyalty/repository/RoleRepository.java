package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

	Page<Role> findAllByNameContainingAllIgnoreCase(String name, Pageable pageable);
	
	Optional<Role> findByNameIgnoreCase(String name);
	
	@EntityGraph(attributePaths = { "authorities" })
	Optional<Role> findWithAuthoritiesById(String id);
	
}
