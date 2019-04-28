package com.dbs.loyalty.service;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.Feedback;
import com.dbs.loyalty.domain.FeedbackAnswer;
import com.dbs.loyalty.domain.FeedbackCustomer;
import com.dbs.loyalty.domain.FeedbackCustomerId;
import com.dbs.loyalty.domain.FeedbackQuestion;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.FeedbackAnswerRepository;
import com.dbs.loyalty.repository.FeedbackCustomerRepository;
import com.dbs.loyalty.repository.FeedbackRepository;
import com.dbs.loyalty.service.dto.FeedbackAnswerFormDto;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedbackCustomerService {

	private final FeedbackRepository feedbackRepository;
	
	private final FeedbackAnswerRepository feedbackAnswerRepository;
	
	private final FeedbackCustomerRepository feedbackCustomerRepository;
	
	@Transactional
	public FeedbackCustomer save(String id, List<FeedbackAnswerFormDto> feedbackAnswerDtos) throws NotFoundException {
		Optional<FeedbackCustomer> current = feedbackCustomerRepository.findWithAnswersById(id, SecurityUtil.getId());
		
		if(!current.isPresent()) {
			Map<Integer, String> map = findQuestionMap(id);
			Set<FeedbackAnswer> answers = new HashSet<>();
			
			FeedbackCustomer feedbackCustomer = new FeedbackCustomer();
			feedbackCustomer.setId(new FeedbackCustomerId(id, SecurityUtil.getId()));
			feedbackCustomer.setCreatedDate(Instant.now());
			feedbackCustomer = feedbackCustomerRepository.save(feedbackCustomer);
			
			for(FeedbackAnswerFormDto dto : feedbackAnswerDtos) {
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
	
	public Page<FeedbackCustomer> findWithCustomerAndAnswersByFeedbackId(String feedbackId, Pageable pageable){
		return feedbackCustomerRepository.findWithCustomerAndAnswersByFeedbackId(feedbackId, pageable);
	}
	
	public Optional<FeedbackCustomer> findWithAnswersById(String feedbackId, String customerId){
		return feedbackCustomerRepository.findWithAnswersById(feedbackId, customerId);
	}

	private Map<Integer, String> findQuestionMap(String id) throws NotFoundException{
		Map<Integer, String> map = new HashMap<Integer, String>();
		Optional<Feedback> feedback = feedbackRepository.findWithQuestionsById(id);
		
		if(feedback.isPresent()) {
			for(FeedbackQuestion question: feedback.get().getQuestions()) {
				map.put(question.getQuestionNumber(), question.getQuestionText());
			}
		}else {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
			throw new NotFoundException(message);
		}
		
		return map;
	}
	
}
