package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.Feedback;
import com.dbs.loyalty.domain.FeedbackAnswer;
import com.dbs.loyalty.domain.FeedbackCustomer;
import com.dbs.loyalty.domain.FeedbackCustomerId;
import com.dbs.loyalty.domain.FeedbackQuestion;
import com.dbs.loyalty.repository.FeedbackAnswerRepository;
import com.dbs.loyalty.repository.FeedbackCustomerRepository;
import com.dbs.loyalty.repository.FeedbackRepository;
import com.dbs.loyalty.service.dto.FeedbackAnswerDto;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedbackCustomerService {

	private final FeedbackRepository feedbackRepository;
	
	private final FeedbackAnswerRepository feedbackAnswerRepository;
	
	private final FeedbackCustomerRepository feedbackCustomerRepository;
	
	@Transactional
	public FeedbackCustomer save(String id, List<FeedbackAnswerDto> feedbackAnswerDtos) {
		Optional<FeedbackCustomer> current = feedbackCustomerRepository.findWithAnswersById(id, SecurityUtil.getId());
		
		if(!current.isPresent()) {
			Map<Integer, String> map = findQuestionMap(id);
			Set<FeedbackAnswer> answers = new HashSet<>();
			
			FeedbackCustomer feedbackCustomer = new FeedbackCustomer();
			feedbackCustomer.setId(new FeedbackCustomerId(id, SecurityUtil.getId()));
			feedbackCustomer.setCreatedDate(Instant.now());
			feedbackCustomer = feedbackCustomerRepository.save(feedbackCustomer);
			
			for(FeedbackAnswerDto dto : feedbackAnswerDtos) {
				FeedbackAnswer answer = new FeedbackAnswer();
				answer.setQuestionNumber(dto.getQuestionNumber());
				answer.setQuestionText(map.get(dto.getQuestionNumber()));
				answer.setQuestionAnswer(dto.getQuestionAnswer());
				answer.setFeedbackCustomer(feedbackCustomer);
				answers.add(feedbackAnswerRepository.save(answer));
			}
			
			feedbackCustomer.setAnswers(answers);
			return feedbackCustomer;
		}else {
			return current.get();
		}
	}

	private Map<Integer, String> findQuestionMap(String id){
		Map<Integer, String> map = new HashMap<Integer, String>();
		Optional<Feedback> feedback = feedbackRepository.findWithQuestionsById(id);
		
		if(feedback.isPresent()) {
			for(FeedbackQuestion question: feedback.get().getQuestions()) {
				map.put(question.getQuestionNumber(), question.getQuestionText());
			}
		}
		
		return map;
	}
	
}
