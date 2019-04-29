package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.EVENT;

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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.domain.EventCustomer;
import com.dbs.loyalty.domain.FeedbackCustomer;
import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.FilePdfTask;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.EventCustomerService;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.FeedbackCustomerService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PdfService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.Response;
import com.dbs.loyalty.web.validator.EventValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/event")
public class EventController extends AbstractPageController {

	private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy,HH:ss");
	
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	
	private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:ss");
	
	private final ImageService imageService;
	
	private final PdfService pdfService;
	
	private final EventService eventService;
	
	private final EventCustomerService eventCustomerService;
	
	private final FeedbackCustomerService feedbackCustomerService;

	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping
	public String viewEvents(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "title");
		Page<Event> page = eventService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/event";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "event/event-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{id}/customer")
	public String viewEventCustomers(@RequestParam Map<String, String> params, @PathVariable String id, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "customer.name");
		Page<EventCustomer> page = eventCustomerService.findWithCustomerByEventId(id, getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/event" + id + "/customer";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "event/event-customer";
		}
	}
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{eventId}/feedback")
	public String viewEventFeedback(@RequestParam Map<String, String> params, @PathVariable String eventId, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "customer.name");
		Page<FeedbackCustomer> page = feedbackCustomerService.findWithCustomerAndAnswersByFeedbackId(eventId, getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/event" + eventId + "/feedback";
		}else {
			request.setAttribute("id", eventId);
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "event/event-feedback";
		}
	}
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{eventId}/feedback/{customerId}")
	public String viewEventFeedbackDetail(@PathVariable String eventId, @PathVariable String customerId, ModelMap model) {
		Optional<FeedbackCustomer> current = feedbackCustomerService.findWithAnswersById(eventId, customerId);
		
		if (current.isPresent()) {
			model.addAttribute("feedbackcustomer", current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(eventId + "," + customerId));
		}
		
		model.addAttribute("eventId", eventId);
		return "event/event-feedback-detail";
	}
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{id}/detail")
	public String viewEventDetail(ModelMap model, @PathVariable String id){
		getById(model, id);
		return "event/event-detail";
	}

	@PreAuthorize("hasRole('EVENT_MK')")
	@GetMapping("/{id}")
	public String viewEventForm(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(EVENT, new Event());
		} else {
			getById(model, id);
		}
		
		return "event/event-form";
	}

	@Transactional
	@PreAuthorize("hasRole('EVENT_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<Response> saveEvent(@ModelAttribute @Valid Event event, BindingResult result) throws BadRequestException, IOException, NotFoundException, ParseException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		}
		
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
				
				current.get().setPending(true);
				eventService.save(current.get());
			}else {
				throw new NotFoundException();
			}
		}

		return taskIsSavedResponse(EVENT, event.getTitle(), UrlUtil.getUrl(EVENT));
	}

	@Transactional
	@PreAuthorize("hasRole('EVENT_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Response> deleteEvent(@PathVariable String id) throws JsonProcessingException, NotFoundException{
		Optional<Event> current = eventService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(EVENT, current.get());
			
			current.get().setPending(true);
			eventService.save(current.get());
			return taskIsSavedResponse(EVENT, current.get().getTitle(), UrlUtil.getUrl(EVENT));
		}else {
			throw new NotFoundException();
		}
	}

	@InitBinder("event")
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
		System.out.println(dateString + "," + timePeriod);
		return DATETIME_FORMAT.parse(dateString + "," + timePeriod);
	}

	private String getTime(Date date) {
		return TIME_FORMAT.format(date);
	}
	
}
