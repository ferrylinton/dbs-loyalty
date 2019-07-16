package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.FeedbackAnswer;

public interface FeedbackAnswerRepository extends JpaRepository<FeedbackAnswer, String>{

	
}
