package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;
import static com.dbs.loyalty.config.constant.SwaggerConstant.FEEDBACK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.Feedback;
import com.dbs.loyalty.domain.FeedbackCustomer;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.FeedbackCustomerService;
import com.dbs.loyalty.service.FeedbackService;
import com.dbs.loyalty.service.dto.FeedbackAnswerDto;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing PromoCategory.
 */
@Api(tags = { FEEDBACK })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class FeedbackRestController {

    private final FeedbackService feedbackService;
    
    private final FeedbackCustomerService feedbackCustomerService;
   
    /**
     * GET  /feedbacks : get feedback by id.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of feedback in body
     * @throws NotFoundException 
     * @throws IOException 
     */
    @ApiOperation(
    		nickname="GetFeedbackById", 
    		value="GetFeedbackById", 
    		notes="Get feedback by id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoCategory.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/feedbacks/{id}")
    public ResponseEntity<Feedback> getFeedback(
    		@ApiParam(name = "id", value = "Event Id / Feedback Id", example = "93643790-8aca-4b62-9dad-f3f818e3de14")
    		@PathVariable String id) throws NotFoundException, IOException{
    	
    	Optional<Feedback> current = feedbackService.findWithQuestionsById(id);
		
		if(current.isPresent()) {
			return ResponseEntity.ok().body(current.get());
		}else {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}
    }

    @ApiOperation(
    		nickname="AddFeedbackCustomer", 
    		value="AddFeedbackCustomer", 
    		notes="Add feedback customer",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoCategory.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/feedbacks/{id}")
    public ResponseEntity<FeedbackCustomer> saveFeedbackCustomer(
    		@ApiParam(name = "id", value = "Event Id / Feedback Id", example = "93643790-8aca-4b62-9dad-f3f818e3de14")
    		@PathVariable String id,
    		@Valid @RequestBody List<FeedbackAnswerDto> feedbackAnswerDtos) throws NotFoundException{
    	
    	FeedbackCustomer feedbackCustomer = feedbackCustomerService.save(id, feedbackAnswerDtos);
    	return ResponseEntity.ok().body(feedbackCustomer);
    }
    
}
