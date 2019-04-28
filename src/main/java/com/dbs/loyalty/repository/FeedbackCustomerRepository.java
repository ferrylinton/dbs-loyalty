package com.dbs.loyalty.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dbs.loyalty.domain.FeedbackCustomer;
import com.dbs.loyalty.domain.FeedbackCustomerId;

public interface FeedbackCustomerRepository extends JpaRepository<FeedbackCustomer, FeedbackCustomerId>{

	@Query(value = "select f from FeedbackCustomer f "
			+ "join fetch f.answers a "
			+ "where f.id.feedbackId = ?1 and f.id.customerId = ?2 ")
	Optional<FeedbackCustomer> findWithAnswersById(String feedbackId, String customerId);
	
	@Query(value = "select f from FeedbackCustomer f "
			+ "join fetch f.customer c "
			+ "join fetch f.answers a "
			+ "where f.id.feedbackId = ?1 ",
			countQuery = "select count(f) from FeedbackCustomer f "
					+ "where f.id.feedbackId = ?1 ")
	Page<FeedbackCustomer> findWithCustomerAndAnswersByFeedbackId(String feedbackId, Pageable pageable);
	
}
