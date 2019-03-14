package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.User;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User>{

	@EntityGraph(attributePaths = { "role", "role.authorities"})
	Page<User> findAll(Pageable pageable);

	@EntityGraph(attributePaths = { "role", "role.authorities"})
	Optional<User> findWithRoleByUsername(String username);
	
	@EntityGraph(attributePaths = { "role", "role.authorities"})
	Optional<User> findWithRoleById(String id);
	
	Optional<User> findByUsernameIgnoreCase(String username);
	
	Optional<User> findByEmailIgnoreCase(String email);
	
}
