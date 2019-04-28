package com.dbs.loyalty.service;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.EventCustomer;
import com.dbs.loyalty.domain.CustomerEventId;
import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.domain.enumeration.EventAnswer;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.EventCustomerRepository;
import com.dbs.loyalty.repository.EventRepository;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventCustomerService {

	private final EventRepository eventRepository;
	
	private final EventCustomerRepository eventCustomerRepository;
	
	public SuccessResponse save(String eventId, String answer) throws NotFoundException {
		Optional<Event> event = eventRepository.findById(eventId);
		
		if(!event.isPresent()) {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}else {
			CustomerEventId id = new CustomerEventId(SecurityUtil.getId(), eventId);
			Optional<EventCustomer> current = eventCustomerRepository.findById(id);
			
			if(current.isPresent()) {
				return new SuccessResponse(String.format("Data [%s,%s] is already exist", eventId, current.get().getEventAnswer().toString()));
			}else {
				EventCustomer customerEvent = new EventCustomer();
				customerEvent.setId(id);
				customerEvent.setEventAnswer(EventAnswer.valueOf(answer));
				customerEvent.setCreatedDate(Instant.now());
				customerEvent = eventCustomerRepository.save(customerEvent);
				
				return new SuccessResponse(String.format("Data [%s,%s] is saved",eventId , answer));
			}
		}
	}
	
	public Page<EventCustomer> findWithCustomerByEventId(String eventId, Pageable pageable){
		return eventCustomerRepository.findWithCustomerByEventId(eventId, pageable);
	}
	
}
