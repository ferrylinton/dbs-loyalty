package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.EventRepository;
import com.dbs.loyalty.service.dto.EventDto;
import com.dbs.loyalty.service.dto.EventFormDto;
import com.dbs.loyalty.service.dto.EventViewDto;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.mapper.EventMapper;
import com.dbs.loyalty.service.specification.EventSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventService{

	private final EventRepository eventRepository;
	
	private final ObjectMapper objectMapper;
	
	private final EventMapper eventMapper;

	public Optional<EventDto> findById(String id){
		return eventRepository.findById(id).map(event -> eventMapper.toDto(event));
	}
	
	public Optional<EventViewDto> findViewById(String id){
		return eventRepository.findById(id).map(event -> eventMapper.toViewDto(event));
	}

	public Page<EventDto> findAll(Pageable pageable, HttpServletRequest request) {
		return eventRepository.findAll(EventSpecification.getSpec(request), pageable)
				.map(event -> eventMapper.toDto(event));
	}

	
	public boolean isTitleExist(String id, String title) {
		Optional<Event> event = eventRepository.findByTitleIgnoreCase(title);

		if (event.isPresent()) {
			return (id == null) || (!id.equals(event.get().getId()));
		}else {
			return false;
		}
	}

	public String execute(TaskDto taskDto) throws IOException {
		EventFormDto eventDto = objectMapper.readValue((taskDto.getTaskOperation() == TaskOperation.DELETE) ? taskDto.getTaskDataOld() : taskDto.getTaskDataNew(), EventFormDto.class);
		
		if(taskDto.isVerified()) {
			Event event = eventMapper.toEntity(eventDto);
			
			if(taskDto.getTaskOperation() == TaskOperation.ADD) {
				event.setCreatedBy(taskDto.getMaker());
				event.setCreatedDate(taskDto.getMadeDate());
				eventRepository.save(event);
			}else if(taskDto.getTaskOperation() == TaskOperation.MODIFY) {
				event.setLastModifiedBy(taskDto.getMaker());
				event.setLastModifiedDate(taskDto.getMadeDate());
				eventRepository.save(event);
			}else if(taskDto.getTaskOperation() == TaskOperation.DELETE) {
				eventRepository.delete(event);
			}
		}

		return eventDto.getTitle();
	}
	
}
