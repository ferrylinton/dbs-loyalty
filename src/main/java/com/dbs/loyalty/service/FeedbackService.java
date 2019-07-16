package com.dbs.loyalty.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Feedback;
import com.dbs.loyalty.repository.FeedbackRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedbackService {
	
	private static final String DEFAULT_FEEDBACK_ID = "default";

	private final FeedbackRepository feedbackRepository;
	
	public Optional<Feedback> getDefault(){
		return feedbackRepository.findById(DEFAULT_FEEDBACK_ID);
	}
	
}
