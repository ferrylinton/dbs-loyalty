package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.TOAST;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.EVENT;
import static com.dbs.loyalty.config.constant.EntityConstant.ROLE;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.FilePdfTask;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PdfService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.web.validator.EventValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/event")
public class EventController extends AbstractPageController {
	
	private static final String REDIRECT = "redirect:/event";
	
	private static final String VIEW = "event/event-view";
	
	private static final String DETAIL = "event/event-detail";
	
	private static final String FORM = "event/event-form";
	
	private static final String SORT_BY = "title";

	private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy,HH:ss");
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:ss");
	
	private final ImageService imageService;
	
	private final PdfService pdfService;
	
	private final EventService eventService;

	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping
	public String viewEvents(@ModelAttribute(TOAST) String toast, @RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, SORT_BY);
		Page<Event> page = eventService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
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
		if (id.equals(ZERO)) {
			model.addAttribute(EVENT, new Event());
		} else {
			getById(model, id);
		}
		
		return FORM;
	}

	@Transactional
	@PreAuthorize("hasRole('EVENT_MK')")
	@PostMapping
	public String saveEvent(@ModelAttribute(EVENT) @Valid Event event, BindingResult result, RedirectAttributes attributes) throws IOException, ParseException {
		if (result.hasErrors()) {
			return FORM;
		}else {
			event.setStartPeriod(setTime(event.getStartPeriod(), event.getTimePeriod()));
			event.setEndPeriod(setTime(event.getEndPeriod(), event.getTimePeriod()));
			
			if(event.getId() == null) {
				FileImageTask fileImageTask = imageService.add(event.getMultipartFileImage());
				event.setImage(fileImageTask.getId());
				
				FilePdfTask filePdfTask = pdfService.add(event.getMultipartFileMaterial());
				event.setMaterial(filePdfTask.getId());
				
				taskService.saveTaskAdd(EVENT, event);
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
						event.setImage(filePdfTask.getId());
					}
					
					current.get().setImage(event.getId());
					current.get().setMaterial(event.getId());
					taskService.saveTaskModify(EVENT, current.get(), event);
					eventService.save(true, event.getId());
				}
			}

			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(ROLE, event.getTitle()));
			return REDIRECT;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('EVENT_MK')")
	@PostMapping("/delete/{id}")
	public String deleteEvent(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		Optional<Event> current = eventService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(EVENT, current.get());
			eventService.save(true, id);
			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(ROLE, current.get().getTitle()));
		}
		
		return REDIRECT;
	}

	@InitBinder(EVENT)
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new EventValidator(eventService));
	}

	private void getById(ModelMap model, String id){
		Optional<Event> current = eventService.findById(id);
		
		if (current.isPresent()) {
			Event event = current.get();
			event.setTimePeriod(getTime(event.getStartPeriod()));
			model.addAttribute(EVENT, event);
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}
	
	private Date setTime(Date date, String timePeriod) throws ParseException {
		String dateString = DATE_FORMAT.format(date);
		return DATETIME_FORMAT.parse(dateString + "," + timePeriod);
	}

	private String getTime(Date date) {
		return TIME_FORMAT.format(date);
	}
	
}
