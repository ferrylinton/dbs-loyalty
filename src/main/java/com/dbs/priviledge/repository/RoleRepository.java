package com.dbs.priviledge.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.priviledge.domain.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

	Page<Role> findAllByNameContainingAllIgnoreCase(String name, Pageable pageable);
	
	Optional<Role> findByNameIgnoreCase(String name);
	
}
