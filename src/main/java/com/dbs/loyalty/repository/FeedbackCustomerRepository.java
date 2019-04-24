package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.FeedbackCustomer;
import com.dbs.loyalty.domain.FeedbackCustomerId;

public interface FeedbackCustomerRepository extends JpaRepository<FeedbackCustomer, FeedbackCustomerId>{

	@Query(value = "select f from FeedbackCustomer f "
			+ "join fetch f.answers a "
			+ "where f.id.feedbackId = ?1 and f.id.customerId = ?2 "
			+ "order by a.questionNumber asc ")
	Optional<FeedbackCustomer> findWithAnswersById(String feedbackId, String customerId);
	
}
