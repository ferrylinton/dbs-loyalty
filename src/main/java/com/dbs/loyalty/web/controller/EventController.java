package com.dbs.loyalty.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.evt.Event;
import com.dbs.loyalty.domain.evt.Feedback;
import com.dbs.loyalty.domain.file.FileImageTask;
import com.dbs.loyalty.domain.file.FilePdfTask;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.FeedbackService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PdfService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;
import com.dbs.loyalty.web.validator.EventValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/event")
public class EventController {
	
	private static final String REDIRECT 	= "redirect:/event";
	
	private static final String VIEW 		= "event/event-view";
	
	private static final String DETAIL 		= "event/event-detail";
	
	private static final String FORM		= "event/event-form";
	
	private static final String SORT_BY 	= "title";
	
	private final ImageService imageService;
	
	private final PdfService pdfService;
	
	private final EventService eventService;

	private final TaskService taskService;
	
	private final FeedbackService feedbackService;

	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping
	public String viewEvents(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<Event> page = eventService.findAll(params, PageUtil.getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
			return VIEW;
		}
	}
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{id}/detail")
	public String viewEventDetail(ModelMap model, @PathVariable String id){
		getById(model, id);
		return DETAIL;
	}

	@PreAuthorize("hasRole('EVENT_MK')")
	@GetMapping("/{id}")
	public String viewEventForm(ModelMap model, @PathVariable String id){
		if (id.equals(Constant.ZERO)) {
			Event event = new Event();
			event.setStartPeriod(Instant.now());
			event.setEndPeriod(Instant.now());
			
			model.addAttribute(DomainConstant.EVENT, new Event());
		} else {
			getById(model, id);
		}
		
		return FORM;
	}

	@Transactional
	@PreAuthorize("hasRole('EVENT_MK')")
	@PostMapping
	public String saveEvent(@ModelAttribute(DomainConstant.EVENT) @Valid Event event, BindingResult result, RedirectAttributes attributes) throws IOException, ParseException {
		if(result.hasErrors()) {
			return FORM;
		}else {
			Optional<Feedback> feedback = feedbackService.getDefault();
			
			if(feedback.isPresent()) {
				event.setFeedback(feedback.get());
			}

			if(event.getId() == null) {
				FileImageTask fileImageTask = imageService.add(event.getMultipartFileImage());
				event.setImage(fileImageTask.getId());
				
				FilePdfTask filePdfTask = pdfService.add(event.getMultipartFileMaterial());
				event.setMaterial(filePdfTask.getId());
				
				taskService.saveTaskAdd(DomainConstant.EVENT, event);
			}else {
				Optional<Event> current = eventService.findById(event.getId());
				
				if(current.isPresent()) {
					if(event.getMultipartFileImage().isEmpty()) {
						event.setImage(event.getId());
					}else {
						FileImageTask fileImageTask = imageService.add(event.getMultipartFileImage());
						event.setImage(fileImageTask.getId());
					}
					
					if(event.getMultipartFileMaterial().isEmpty()) {
						event.setMaterial(event.getId());
					}else {
						FilePdfTask filePdfTask = pdfService.add(event.getMultipartFileMaterial());
						event.setMaterial(filePdfTask.getId());
					}
					
					current.get().setImage(event.getId());
					current.get().setMaterial(event.getId());
					taskService.saveTaskModify(DomainConstant.EVENT, current.get(), event);
					eventService.save(true, event.getId());
				}
			}

			attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSavedMessage(DomainConstant.EVENT, event.getTitle()));
			return REDIRECT;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('EVENT_MK')")
	@PostMapping("/delete/{id}")
	public String deleteEvent(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		Optional<Event> current = eventService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(DomainConstant.EVENT, current.get());
			eventService.save(true, id);
			attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSavedMessage(DomainConstant.EVENT, current.get().getTitle()));
		}
		
		return REDIRECT;
	}

	@InitBinder(DomainConstant.EVENT)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new EventValidator(eventService));
	}

	private void getById(ModelMap model, String id){
		Optional<Event> current = eventService.findById(id);
		
		if (current.isPresent()) {
			Event event = current.get();
			model.addAttribute(DomainConstant.EVENT, event);
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
	}

}
