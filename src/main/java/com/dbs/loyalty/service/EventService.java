package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.domain.Feedback;
import com.dbs.loyalty.domain.FeedbackQuestion;
import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.FilePdfTask;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.model.Pair;
import com.dbs.loyalty.repository.EventRepository;
import com.dbs.loyalty.service.specification.EventSpec;
import com.dbs.loyalty.util.JsonUtil;
import com.dbs.loyalty.util.TaskUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EventService{

	private static final String TYPE = Event.class.getSimpleName();
	
	private final EventRepository eventRepository;
	
	private final ImageService imageService;
	
	private final PdfService pdfService;
	
	private final FeedbackService feedbackService;

	private final TaskService taskService;
	
	public Optional<Event> findById(String id){
		return eventRepository.findById(id);
	}
	
	public Event getOne(String id){
		return eventRepository.getOne(id);
	}
	
	public Optional<Event> findWithFeedbackById(String id) throws IOException{
		Optional<Event> event = eventRepository.findWithFeedbackById(id);
		
		if(event.isPresent()) {
			setQuestionOptions(event.get().getFeedback());
		}
		
		return event;
	}

	public Page<Event> findAll(Map<String, String> params, Pageable pageable) {
		return eventRepository.findAll(new EventSpec(params), pageable);
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

	@Transactional
	public void taskSave(Event newEvent) throws IOException {
		Optional<Feedback> feedback = feedbackService.getDefault();
		
		if(feedback.isPresent()) {
			newEvent.setFeedback(feedback.get());
		}

		if(newEvent.getId() == null) {
			FileImageTask fileImageTask = imageService.add(newEvent.getMultipartFileImage());
			newEvent.setImage(fileImageTask.getId());
			
			FilePdfTask filePdfTask = pdfService.add(newEvent.getMultipartFileMaterial());
			newEvent.setMaterial(filePdfTask.getId());
			
			taskService.saveTaskAdd(TYPE, JsonUtil.toString(newEvent));
		}else {
			Event oldEvent = eventRepository.getOne(newEvent.getId());
			
			if(newEvent.getMultipartFileImage().isEmpty()) {
				newEvent.setImage(newEvent.getId());
			}else {
				FileImageTask fileImageTask = imageService.add(newEvent.getMultipartFileImage());
				newEvent.setImage(fileImageTask.getId());
			}
			
			if(newEvent.getMultipartFileMaterial().isEmpty()) {
				newEvent.setMaterial(newEvent.getId());
			}else {
				FilePdfTask filePdfTask = pdfService.add(newEvent.getMultipartFileMaterial());
				newEvent.setMaterial(filePdfTask.getId());
			}
			
			oldEvent.setImage(newEvent.getId());
			oldEvent.setMaterial(newEvent.getId());
			
			eventRepository.save(true, newEvent.getId());
			taskService.saveTaskModify(TYPE, oldEvent.getId(), JsonUtil.toString(oldEvent), JsonUtil.toString(newEvent));
		}
	}

	@Transactional
	public void taskDelete(Event event) throws JsonProcessingException {
		taskService.saveTaskDelete(TYPE, event.getId(), JsonUtil.toString(event));
		eventRepository.save(true, event.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		Event event = TaskUtil.getObject(task, Event.class);
		
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
			eventRepository.save(false, event.getId());
		}

		taskService.confirm(task);
		return event.getTitle();
	}
	
	private void setQuestionOptions(Feedback feedback) throws IOException{
		for(FeedbackQuestion question: feedback.getQuestions()) {
			if(question.getQuestionOption() != null) {
				List<Pair<String, String>> questionOptions = JsonUtil.readValue(question.getQuestionOption(), new TypeReference<List<Pair<String, String>>>() {});
				question.setQuestionOptions(questionOptions);
			}
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Task task, String error) {
		try {
			Event event = TaskUtil.getObject(task, Event.class);
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				eventRepository.save(false, event.getId());
			}

			taskService.save(task, error);
			return error;
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}

}
