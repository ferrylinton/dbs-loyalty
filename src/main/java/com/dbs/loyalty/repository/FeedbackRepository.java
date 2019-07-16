package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, String>{

	@Query(value = "select f from Feedback f "
			+ "join fetch f.events e "
			+ "join fetch f.questions q "
			+ "where e.id = ?1 ")
	Optional<Feedback> findWithQuestionsByEventId(String id);
	
}
