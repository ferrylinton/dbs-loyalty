package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.domain.EventCustomer;
import com.dbs.loyalty.domain.EventCustomerId;
import com.dbs.loyalty.domain.enumeration.EventAnswer;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.EventCustomerRepository;
import com.dbs.loyalty.repository.EventRepository;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventCustomerService {

	private final EventRepository eventRepository;
	
	private final EventCustomerRepository eventCustomerRepository;
	
	public Response save(String eventId, String answer) throws NotFoundException, BadRequestException {
		Optional<Event> event = eventRepository.findById(eventId);
		
		if(!event.isPresent()) {
			String message = MessageUtil.getNotFoundMessage(SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}else {
			EventCustomerId id = new EventCustomerId();
			id.setEventId(eventId);
			id.setCustomerId(SecurityUtil.getId());
			
			Optional<EventCustomer> current = eventCustomerRepository.findById(id);
			
			if(current.isPresent()) {
				return new Response(MessageConstant.DATA_IS_ALREADY_EXIST);
			}else {
				EventCustomer customerEvent = new EventCustomer();
				customerEvent.setId(id);
				customerEvent.setEventAnswer(getEventAnswer(answer));
				customerEvent.setCreatedDate(Instant.now());
				customerEvent = eventCustomerRepository.save(customerEvent);
				
				return new Response(MessageConstant.DATA_IS_SAVED);
			}
		}
	}
	
	public Page<EventCustomer> findEventCustomers(String eventId, Map<String, String> params, Pageable pageable){
		if(params.containsKey(Constant.KY_PARAM)) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			return eventCustomerRepository.findByEventIdAndKeyword(eventId, keyword, pageable);
		}else {
			return eventCustomerRepository.findByEventId(eventId, pageable);
		}
	}
	
	private EventAnswer getEventAnswer(String answer) throws BadRequestException {
		try {
			return EventAnswer.valueOf(answer.toUpperCase());
		} catch (IllegalArgumentException e) { 
			throw new BadRequestException(String.format("%s is not valid", answer));
		}
	}
}
