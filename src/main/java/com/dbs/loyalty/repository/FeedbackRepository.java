package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, String>{

}
