package com.dbs.loyalty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.evt.Event;

public interface EventRepository extends JpaRepository<Event, String>, JpaSpecificationExecutor<Event>{

	Optional<Event> findByTitleIgnoreCase(String title);
	
	@Query(value = "select e from Event e "
			+ "join fetch e.feedback f "
			+ "join fetch f.questions q "
			+ "where e.id = ?1")
	Optional<Event> findWithFeedbackById(String id);

	@Query(value = "select e from Event e "
			+ "where e.startPeriod > current_date()")
	List<Event> findUpcomingEvent();
	
	@Query(value = "select e from Event e "
			+ "where e.endPeriod < current_date()")
	List<Event> findPreviousEvent();
	
	@Modifying
	@Query("update Event e set e.pending = ?1 where e.id = ?2")
	void save(boolean pending, String id);
	
}
