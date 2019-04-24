package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Feedback;
import com.dbs.loyalty.domain.FeedbackQuestion;
import com.dbs.loyalty.model.Pair;
import com.dbs.loyalty.repository.FeedbackRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedbackService{

	private final FeedbackRepository feedbackRepository;

	private final ObjectMapper objectMapper;

	public Optional<Feedback> findWithQuestionsById(String id) throws IOException{
		Optional<Feedback> feedback = feedbackRepository.findWithQuestionsById(id);
		
		if(feedback.isPresent()) {
			for(FeedbackQuestion question: feedback.get().getQuestions()) {
				if(question.getQuestionOption() != null) {
					List<Pair<String, String>> questionOptions = objectMapper.readValue(question.getQuestionOption(), new TypeReference<List<Pair<String, String>>>() {});
					question.setQuestionOptions(questionOptions);
				}
			}
		}
		
		return feedback;
	}
	
}
