package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.EntityConstant.EVENT;
import static com.dbs.loyalty.config.constant.MessageConstant.ENTITY_WITH_VALUE_NOT_FOUND;
import static com.dbs.loyalty.config.constant.SwaggerConstant.FEEDBACK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.Event;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.EventService;
import com.dbs.loyalty.service.FeedbackCustomerService;
import com.dbs.loyalty.service.dto.FeedbackAnswerDto;
import com.dbs.loyalty.service.dto.FeedbackAnswerFormDto;
import com.dbs.loyalty.service.mapper.FeedbackAnswerMapper;
import com.dbs.loyalty.util.MessageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Feedback Answer
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { FEEDBACK })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FeedbackAnswerRestController {

	private final EventService eventService;
	
    private final FeedbackCustomerService feedbackCustomerService;
    
    private final FeedbackAnswerMapper feedbackAnswerMapper;

    @ApiOperation(
    		nickname		= "AddFeedbackCustomer", 
    		value			= "AddFeedbackCustomer", 
    		notes			= "Add feedback customer",
    		produces		= MediaType.APPLICATION_JSON_VALUE, 
    		authorizations	= { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = FeedbackAnswerDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/feedbackanswers/{eventId}")
    public ResponseEntity<List<FeedbackAnswerDto>> addFeedbackCustomer(
    		@ApiParam(name = "eventId", value = "Event Id", example = "77UTTDWJX3zNWABg9ixZX9")
    		@PathVariable String eventId,
    		@Valid @RequestBody List<FeedbackAnswerFormDto> feedbackAnswerFormDtos) throws NotFoundException{
    	
    	Optional<Event> event = eventService.findById(eventId);
    	
    	if(event.isPresent()) {
    		List<FeedbackAnswerDto> answers = feedbackCustomerService
        			.save(eventId, feedbackAnswerFormDtos)
        			.getAnswers()
        			.stream()
        			.map(feedbackAnswer -> feedbackAnswerMapper.toDto(feedbackAnswer))
        			.collect(Collectors.toList());
        	
        	Collections.sort(answers);
        	return ResponseEntity.ok().body(answers);
    	}else {
    		String message = MessageUtil.getMessage(ENTITY_WITH_VALUE_NOT_FOUND, EVENT, eventId);
			throw new NotFoundException(message);
    	}
    	
    }
    
}