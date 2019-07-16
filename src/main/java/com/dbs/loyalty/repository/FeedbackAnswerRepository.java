package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.evt.FeedbackAnswer;

public interface FeedbackAnswerRepository extends JpaRepository<FeedbackAnswer, String>{

	
}
