package com.dbs.loyalty.web.controller.rest;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.FeedbackQuestionService;
import com.dbs.loyalty.service.dto.FeedbackQuestionDto;
import com.dbs.loyalty.service.mapper.FeedbackQuestionMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Feedback Question
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.FEEDBACK })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/feedback-questions")
public class FeedbackQuestionRestController {
	
	public static final String GET_FEEDBACK_QUESTIONS = "GetFeedbackQuestions";

    private final FeedbackQuestionService feedbackQuestionService;

    private final FeedbackQuestionMapper feedbackQuestionMapper;

   
    /**
     * GET  /api/feedbackquestions : get Feedback Questions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of Feedback Questions
     * @throws IOException 
     * @throws NotFoundException 
     */
    @ApiOperation(
    		nickname = GET_FEEDBACK_QUESTIONS, 
    		value = GET_FEEDBACK_QUESTIONS, 
    		notes = "Get feedback questons",
    		produces = MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value = { @ApiResponse(code=200, message="OK", response = FeedbackQuestionDto.class, responseContainer="list") })
    @PreAuthorize("hasRole('CUSTOMER')")
    @EnableLogAuditCustomer(operation=GET_FEEDBACK_QUESTIONS)
    @GetMapping("/{eventId}")
    public List<FeedbackQuestionDto> getFeedbackById(
    		@ApiParam(name = "eventId", value = "Event Id", example = "77UTTDWJX3zNWABg9ixZX9")
    		@PathVariable String eventId,
    		HttpServletRequest request, HttpServletResponse response) throws IOException, NotFoundException{
    	
    	List<FeedbackQuestionDto> questions = feedbackQuestionService
    			.findByEventId(eventId)
    			.stream()
				.map(question -> feedbackQuestionMapper.toDto(question))
				.collect(Collectors.toList());

		Collections.sort(questions);
		return questions;
    }

}
