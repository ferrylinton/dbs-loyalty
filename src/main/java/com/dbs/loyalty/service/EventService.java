package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.domain.Feedback;
import com.dbs.loyalty.domain.FeedbackQuestion;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.model.Pair;
import com.dbs.loyalty.repository.EventRepository;
import com.dbs.loyalty.service.specification.EventSpecification;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventService{

	private final EventRepository eventRepository;
	
	private final ImageService imageService;
	
	private final PdfService pdfService;
	
	private final ObjectMapper objectMapper;
	
	public Optional<Event> findById(String id){
		return eventRepository.findById(id);
	}
	
	public Optional<Event> findWithFeedbackById(String id) throws IOException{
		Optional<Event> event = eventRepository.findWithFeedbackById(id);
		
		if(event.isPresent()) {
			setQuestionOptions(event.get().getFeedback());
		}
		
		return event;
	}

	public Page<Event> findAll(Pageable pageable, HttpServletRequest request) {
		return eventRepository.findAll(EventSpecification.getSpec(request), pageable);
	}

	public List<Event> findUpcomingEvent(){
		return eventRepository.findUpcomingEvent();
	}
	
	public List<Event> findPreviousEvent(){
		return eventRepository.findPreviousEvent();
	}
	
	public boolean isTitleExist(String id, String title) {
		Optional<Event> event = eventRepository.findByTitleIgnoreCase(title);

		if (event.isPresent()) {
			return (id == null) || (!id.equals(event.get().getId()));
		}else {
			return false;
		}
	}

	public Event save(Event event) {
		return eventRepository.save(event);
	}
	
	public String execute(Task task) throws IOException {
		Event event = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), Event.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				event.setCreatedBy(task.getMaker());
				event.setCreatedDate(task.getMadeDate());
				eventRepository.save(event);
				imageService.add(event.getId(), event.getImage(), task.getMaker(), task.getMadeDate());
				pdfService.add(event.getId(), event.getMaterial(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				event.setLastModifiedBy(task.getMaker());
				event.setLastModifiedDate(task.getMadeDate());
				event.setPending(false);
				eventRepository.save(event);
				imageService.update(event.getId(), event.getImage(), task.getMaker(), task.getMadeDate());
				pdfService.update(event.getId(), event.getMaterial(), task.getMaker(), task.getMadeDate());
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				eventRepository.delete(event);
				imageService.delete(event.getId());
				pdfService.delete(event.getId());
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			event.setPending(false);
			eventRepository.save(event);
		}

		return event.getTitle();
	}
	
	private void setQuestionOptions(Feedback feedback) throws IOException{
		for(FeedbackQuestion question: feedback.getQuestions()) {
			if(question.getQuestionOption() != null) {
				List<Pair<String, String>> questionOptions = objectMapper.readValue(question.getQuestionOption(), new TypeReference<List<Pair<String, String>>>() {});
				question.setQuestionOptions(questionOptions);
			}
		}
	}
	
}
