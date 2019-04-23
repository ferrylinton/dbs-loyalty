package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.EVENT;
import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.FilePdf;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.FilePdfService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.EventDto;
import com.dbs.loyalty.service.dto.EventFormDto;
import com.dbs.loyalty.util.ImageUtil;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.validator.EventValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/event")
public class EventController extends AbstractPageController {

	private final ImageService imageService;
	
	private final FilePdfService filePdfService;
	
	private final EventService eventService;

	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping
	public String viewEvents(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "title");
		Page<EventDto> page = eventService.findAll(getPageable(params, order), request);

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
	@GetMapping("/{id}/detail")
	public String viewEventDetail(ModelMap model, @PathVariable String id){
		getById(model, id);
		return "event/event-detail";
	}

	@PreAuthorize("hasRole('EVENT_MK')")
	@GetMapping("/{id}")
	public String viewEventForm(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(EVENT, new EventDto());
		} else {
			getById(model, id);
		}
		
		return "event/event-form";
	}
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{id}/material")
	public ResponseEntity<byte[]> getEventMaterial(@PathVariable String id) throws NotFoundException {
		Optional<FileImage> current = imageService.findOneByEventId(id);
    	
		if(current.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.APPLICATION_PDF);
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(current.get().getBytes());
		}else {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}
	}
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{id}/material/download")
	public ResponseEntity<InputStreamResource> downloadEventMaterial(@PathVariable String id) throws NotFoundException, IOException {
		Optional<FilePdf> current = filePdfService.findOneByEventId(id);
    	
		if(current.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.add("content-disposition", "attachment;filename=" + id + ".pdf");
			ByteArrayResource resource = new ByteArrayResource(current.get().getBytes());
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(new InputStreamResource(resource.getInputStream()));
		}else {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}
	}

	@PreAuthorize("hasRole('EVENT_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<AbstractResponse> saveEvent(@ModelAttribute @Valid EventFormDto eventFormDto, BindingResult result) throws BadRequestException, IOException, NotFoundException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		}
		
		if(eventFormDto.getId() == null) {
			FileImageTask fileImageTask = imageService.save(eventFormDto.getImageFile());
			eventFormDto.setImageFileId(fileImageTask.getId());
			taskService.saveTaskAdd(EVENT, eventFormDto);
		}else {
			Optional<EventDto> current = eventService.findById(eventFormDto.getId());
			
			if(current.isPresent()) {
				if(eventFormDto.getImageFile().isEmpty()) {
					eventFormDto.setImageFileId(eventFormDto.getId());
				}else {
					FileImageTask fileImageTask = imageService.save(eventFormDto.getImageFile());
					eventFormDto.setImageFileId(fileImageTask.getId());
				}
				
				taskService.saveTaskModify(EVENT, current.get(), eventFormDto);
			}else {
				throw new NotFoundException();
			}
		}

		return taskIsSavedResponse(EVENT, eventFormDto.getTitle(), UrlUtil.getUrl(EVENT));
	}

	@PreAuthorize("hasRole('EVENT_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<AbstractResponse> deleteEvent(@PathVariable String id) throws JsonProcessingException, NotFoundException{
		Optional<EventDto> current = eventService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(EVENT, current.get());
			return taskIsSavedResponse(EVENT, current.get().getTitle(), UrlUtil.getUrl(EVENT));
		}else {
			throw new NotFoundException();
		}
	}

	
	@InitBinder("eventDto")
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new EventValidator(eventService));
	}

	private void getById(ModelMap model, String id){
		Optional<EventDto> current = eventService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(EVENT, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}
	
}
