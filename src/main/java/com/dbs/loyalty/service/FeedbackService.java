package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

	public Optional<Feedback> findById(String id) throws IOException{
		Optional<Feedback> feedback = feedbackRepository.findById(id);
		
		if(feedback.isPresent()) {
			for(Map.Entry<String, FeedbackQuestion> entry: feedback.get().getQuestionMap().entrySet()) {
				if(entry.getValue().getQuestionOption() != null) {
					List<Pair<String, String>> questionOptions = objectMapper.readValue(entry.getValue().getQuestionOption(), new TypeReference<List<Pair<String, String>>>() {});
					entry.getValue().setQuestionOptions(questionOptions);
				}
			}
		}
		
		return feedback;
	}

}
