package com.dbs.loyalty.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.evt.FeedbackQuestion;

public interface FeedbackQuestionRepository extends JpaRepository<FeedbackQuestion, String>{

	@Query(value = "select q from FeedbackQuestion q "
			+ "join fetch q.feedback f "
			+ "join fetch f.events e "
			+ "where e.id = ?1 ")
	List<FeedbackQuestion> findByEventId(String eventId);
	
}
