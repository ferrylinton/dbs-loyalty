package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;
import static com.dbs.loyalty.config.constant.SwaggerConstant.EVENT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;

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

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.FilePdf;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.EventCustomerService;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PdfService;
import com.dbs.loyalty.service.dto.EventDto;
import com.dbs.loyalty.service.mapper.EventMapper;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.web.response.SuccessResponse;

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
@Api(tags = { EVENT })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class EventRestController {
	
	private static final String CONTENT_DISPOSITION = "content-disposition";
	
	private static final String CONTENT_DISPOSITION_FORMAT = "attachment;filename=%s.pdf";

    private final EventService eventService;
    
    private final EventCustomerService customerEventService;
    
    private final ImageService imageService;
    
    private final PdfService filePdfService;
    
    private final EventMapper eventMapper;;
   
    /**
     * 
     * @return
     */
	@ApiOperation(
			nickname="GetUpcomingEvent", 
			value="GetUpcomingEvent", 
			notes="Get all upcoming event",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = EventDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/events/upcoming")
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
			nickname="GetPreviousEvent", 
			value="GetPreviousEvent", 
			notes="Get all previous event",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = EventDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/events/previous")
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
    		nickname="GetEventById", 
    		value="GetEventById", 
    		notes="Get event by id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = EventDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/events/{id}")
    public ResponseEntity<EventDto> getEventById(
    		@ApiParam(name = "id", value = "Event Id", example = "77UTTDWJX3zNWABg9ixZX9")
    		@PathVariable String id) throws NotFoundException, IOException{
    	
    	Optional<EventDto> current = eventService
    			.findById(id)
    			.map(event -> eventMapper.toDto(event));
		
		if(current.isPresent()) {
			return ResponseEntity.ok().body(current.get());
		}else {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
			throw new NotFoundException(message);
		}
    }
    
    @ApiOperation(
    		nickname="GetEventImage", 
    		value="GetEventImage", 
    		notes="Get Event Image", 
    		produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/events/{id}/image")
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
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
			throw new NotFoundException(message);
		}
	}
	
	@ApiOperation(
    		nickname="GetEventMaterial", 
    		value="GetEventMaterial", 
    		notes="Get Event Material", 
    		produces= "application/pdf", 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/events/{id}/material")
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
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
			throw new NotFoundException(message);
		}
	}


    @ApiOperation(
    		nickname="AddCustomerEventAnswer", 
    		value="AddCustomerEventAnswer", 
    		notes="Add Customer's event answer",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = SuccessResponse.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/events/{id}/{answer}")
    public ResponseEntity<SuccessResponse> addCustomerEvent(
    		@ApiParam(name = "id", value = "Event Id", example = "10noLnNvqC4SwAUMcJ9GXm")
    		@PathVariable String id,
    		@ApiParam(name = "answer", value = "Customer's answer", example = "NO, YES, MAYBE")
    		@PathVariable String answer) throws NotFoundException{
    	
    	SuccessResponse response = customerEventService.save(id, answer);
    	return ResponseEntity.ok().body(response);
    }
    
}
