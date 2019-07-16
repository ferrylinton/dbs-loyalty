package com.dbs.loyalty.service;

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

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.evt.FeedbackAnswer;
import com.dbs.loyalty.domain.evt.FeedbackCustomer;
import com.dbs.loyalty.domain.evt.FeedbackCustomerId;
import com.dbs.loyalty.domain.evt.FeedbackQuestion;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.FeedbackAnswerRepository;
import com.dbs.loyalty.repository.FeedbackCustomerRepository;
import com.dbs.loyalty.repository.FeedbackQuestionRepository;
import com.dbs.loyalty.service.dto.FeedbackAnswerFormDto;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FeedbackCustomerService {

	private final FeedbackAnswerRepository feedbackAnswerRepository;
	
	private final FeedbackCustomerRepository feedbackCustomerRepository;
	
	private final FeedbackQuestionRepository feedbackQuestionRepository;
	
	@Transactional
	public FeedbackCustomer save(String eventId, List<FeedbackAnswerFormDto> feedbackAnswerDtos) throws NotFoundException {
		Optional<FeedbackCustomer> current = feedbackCustomerRepository.findWithAnswersById(eventId, SecurityUtil.getId());
		
		if(!current.isPresent()) {
			Map<Integer, String> map = findQuestionMap(eventId);
			Set<FeedbackAnswer> answers = new HashSet<>();
		
			FeedbackCustomer feedbackCustomer = new FeedbackCustomer();
			feedbackCustomer.setId(new FeedbackCustomerId(eventId));
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
	
	public Page<FeedbackCustomer> findFeedbackCustomers(String eventId, Map<String, String> params, Pageable pageable){
		if(params.containsKey(Constant.KY_PARAM)) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			return feedbackCustomerRepository.findByEventIdAndKeyword(eventId, keyword, pageable);
		}else {
			return feedbackCustomerRepository.findByEventId(eventId, pageable);
		}
	}
	
	public Optional<FeedbackCustomer> findWithAnswersById(String feedbackId, String customerId){
		return feedbackCustomerRepository.findWithAnswersById(feedbackId, customerId);
	}

	private Map<Integer, String> findQuestionMap(String eventId) throws NotFoundException{
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<FeedbackQuestion> questions = feedbackQuestionRepository.findByEventId(eventId);
		
		for(FeedbackQuestion question: questions) {
			map.put(question.getQuestionNumber(), question.getQuestionText());
		}
		
		return map;
	}
	
}
