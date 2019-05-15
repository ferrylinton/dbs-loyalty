package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Role;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role>{

	Page<Role> findAllByNameContainingAllIgnoreCase(String name, Pageable pageable);
	
	Optional<Role> findByNameIgnoreCase(String name);
	
	@EntityGraph(attributePaths = { "authorities" })
	Optional<Role> findWithAuthoritiesById(String id);
	
	@Modifying
	@Query("update Role r set r.pending = ?1 where r.id = ?2")
	void save(boolean pending, String id);

}
