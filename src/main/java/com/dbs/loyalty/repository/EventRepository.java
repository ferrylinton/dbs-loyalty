package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.Event;

public interface EventRepository extends JpaRepository<Event, String>, JpaSpecificationExecutor<Event>{

	Optional<Event> findByTitleIgnoreCase(String title);

}
