package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.BR;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.response.BadRequestResponse;
import com.dbs.loyalty.web.response.ErrorResponse;
import com.dbs.loyalty.web.response.SuccessResponse;

public abstract class AbstractController {

	protected String dataWithIdNotFound = "message.dataWithIdNotFound";
	
	protected String taskIsSaved = "message.taskIsSaved";
	
	protected String taskIsVerified = "message.taskIsVerified";
	
	protected String taskIsRejected = "message.taskIsRejected";
	
	protected ResponseEntity<AbstractResponse> taskIsSavedResponse(String taskDataType, String val, String resultUrl) {
		taskDataType = MessageService.getMessage(taskDataType);
		String message = MessageService.getMessage(taskIsSaved, taskDataType, val);
		return dataIsSavedResponse(message, resultUrl);
	}
	
	protected ResponseEntity<AbstractResponse> dataIsSavedResponse(String message, String resultUrl) {
		return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(new SuccessResponse(message, resultUrl));
	}

	protected String getNotFoundMessage(String id) {
		return MessageService.getMessage(dataWithIdNotFound, id);
	}

	protected ResponseEntity<BadRequestResponse> badRequestResponse(BindingResult result) {
		List<String> fields = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		
		for (FieldError fieldError : result.getFieldErrors()) {
			fields.add(fieldError.getField());
			builder.append(fieldError.getDefaultMessage()).append(BR);
		}

		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(new BadRequestResponse(builder.toString(), fields));
	}
	
	protected void throwBadRequestResponse(BindingResult result) throws BadRequestException {
		List<String> fields = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		
		for (FieldError fieldError : result.getFieldErrors()) {
			fields.add(fieldError.getField());
			builder.append(fieldError.getDefaultMessage()).append(BR);
		}
		
		throw new BadRequestException(new BadRequestResponse(builder.toString(), fields));
	}

	protected ResponseEntity<AbstractResponse> errorResponse(Exception ex){
		return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorResponse(ex.getLocalizedMessage()));
	}

}
