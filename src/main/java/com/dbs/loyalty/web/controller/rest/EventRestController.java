package com.dbs.loyalty.web.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.FilePdf;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.EventCustomerService;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PdfService;
import com.dbs.loyalty.service.dto.EventDto;
import com.dbs.loyalty.service.mapper.EventMapper;
import com.dbs.loyalty.web.response.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Event
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.EVENT })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/events")
public class EventRestController {
	
	public static final String GET_UPCOMING_EVENT = "GetUpcomingEvent";
	
	public static final String GET_PREVIOUS_EVENT = "GetPreviousEvent";
	
	public static final String GET_EVENT_BY_ID = "GetEventById";
	
	private static final String GET_EVENT_IMAGE = "GetEventImage";
	
	private static final String GET_EVENT_MATERIAL = "GetEventMaterial";
	
	private static final String ADD_CUSTOMER_EVENT_ANSWER = "AddCustomerEventAnswer";
	
	private static final String CONTENT_DISPOSITION = "content-disposition";

	private static final String CONTENT_DISPOSITION_FORMAT = "attachment;filename=%s.pdf";

    private final EventService eventService;
    
    private final EventCustomerService customerEventService;
    
    private final ImageService imageService;
    
    private final PdfService filePdfService;
    
    private final EventMapper eventMapper;;

    /**
     * Get Upcoming Event
     * 
     * @return list of Event
     */
	@ApiOperation(
			nickname=GET_UPCOMING_EVENT, 
			value=GET_UPCOMING_EVENT, 
			notes="Get all upcoming event",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = EventDto.class, responseContainer="list")})
	@LogAuditApi(name=GET_UPCOMING_EVENT)
    @GetMapping("/upcoming")
    public ResponseEntity<List<EventDto>> getUpcomingEvent(){
		List<EventDto> events = eventService
				.findUpcomingEvent()
				.stream()
				.map(event -> eventMapper.toDto(event))
				.collect(Collectors.toList());
		
    	return ResponseEntity
    			.ok()
    			.body(events);
    }
	
	@ApiOperation(
			nickname=GET_PREVIOUS_EVENT, 
			value=GET_PREVIOUS_EVENT, 
			notes="Get all previous event",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = EventDto.class)})
	@LogAuditApi(name=GET_PREVIOUS_EVENT)
    @GetMapping("/previous")
    public ResponseEntity<List<EventDto>> getPreviousEvent(){
		List<EventDto> events = eventService
				.findPreviousEvent()
				.stream()
				.map(event -> eventMapper.toDto(event))
				.collect(Collectors.toList());
		
    	return ResponseEntity
    			.ok()
    			.body(events);
    }
	
    /**
     * GET  /mapper/{id} : get mapper by id.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of feedback in body
     * @throws NotFoundException 
     * @throws IOException 
     */
    @ApiOperation(
    		nickname=GET_EVENT_BY_ID, 
    		value=GET_EVENT_BY_ID, 
    		notes="Get event by id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = EventDto.class)})
    @LogAuditApi(name=GET_EVENT_BY_ID, saveRequest=true)
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(
    		@ApiParam(name = "id", value = "Event Id", example = "77UTTDWJX3zNWABg9ixZX9")
    		@PathVariable String id) throws NotFoundException, IOException{
    	
    	Optional<EventDto> current = eventService
    			.findById(id)
    			.map(event -> eventMapper.toDto(event));
		
		if(current.isPresent()) {
			return ResponseEntity.ok().body(current.get());
		}else {
			throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, DomainConstant.EVENT, id));
		}
    }
    
    @ApiOperation(
    		nickname=GET_EVENT_IMAGE, 
    		value=GET_EVENT_IMAGE, 
    		notes="Get Event Image", 
    		produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @LogAuditApi(name=GET_EVENT_IMAGE, saveRequest=true)
    @GetMapping("/{id}/image")
	public ResponseEntity<byte[]> getEventImage(
    		@ApiParam(name = "id", value = "Event Id", example = "77UTTDWJX3zNWABg9ixZX9")
    		@PathVariable String id) throws NotFoundException, IOException{
		
    	Optional<FileImage> fileImage = imageService.findById(id);
    	
		if(fileImage.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.valueOf(fileImage.get().getContentType()));
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(fileImage.get().getBytes());
		}else {
			throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, DomainConstant.EVENT, id));
		}
	}
	
	@ApiOperation(
    		nickname=GET_EVENT_MATERIAL, 
    		value=GET_EVENT_MATERIAL, 
    		notes="Get Event Material", 
    		produces= "application/pdf", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
	@LogAuditApi(name=GET_EVENT_MATERIAL, saveRequest=true)
    @GetMapping("/{id}/material")
	public ResponseEntity<InputStreamResource> getEventMaterial(
    		@ApiParam(name = "id", value = "Event Id", example = "77UTTDWJX3zNWABg9ixZX9")
    		@PathVariable String id) throws NotFoundException, IOException{
		
		Optional<FilePdf> current = filePdfService.findById(id);
    	
		if(current.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.add(CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT, id));
			ByteArrayResource resource = new ByteArrayResource(current.get().getBytes());
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(new InputStreamResource(resource.getInputStream()));
		}else {
			throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, DomainConstant.EVENT, id));
		}
	}

    @ApiOperation(
    		nickname=ADD_CUSTOMER_EVENT_ANSWER, 
    		value=ADD_CUSTOMER_EVENT_ANSWER, 
    		notes="Add Customer's event answer",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Response.class)})
    @LogAuditApi(name=ADD_CUSTOMER_EVENT_ANSWER, saveRequest=true)
    @PostMapping("/{id}/{answer}")
    public Response addCustomerEvent(
    		@ApiParam(name = "id", value = "Event Id", example = "10noLnNvqC4SwAUMcJ9GXm")
    		@PathVariable String id,
    		@ApiParam(name = "answer", value = "Customer's answer", example = "NO, YES, MAYBE")
    		@PathVariable String answer) throws NotFoundException, BadRequestException{
    	
    	return customerEventService.save(id, answer);
    }
    
}
