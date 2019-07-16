package com.dbs.loyalty.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.FeedbackQuestion;
import com.dbs.loyalty.repository.FeedbackQuestionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedbackQuestionService {

	private final FeedbackQuestionRepository feedbackQuestionRepository;
	
	public List<FeedbackQuestion> findByEventId(String eventId){
		List<FeedbackQuestion> questions = feedbackQuestionRepository.findByEventId(eventId);
		Collections.sort(questions);
		return questions;
	}
	
}
