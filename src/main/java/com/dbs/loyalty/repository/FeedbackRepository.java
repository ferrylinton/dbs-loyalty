package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, String>{

	@Query(value = "select f from Feedback f "
			+ "join fetch f.questions q "
			+ "where f.id = ?1 "
			+ "order by q.questionNumber asc ")
	Optional<Feedback> findWithQuestionsById(String id);
	
}
