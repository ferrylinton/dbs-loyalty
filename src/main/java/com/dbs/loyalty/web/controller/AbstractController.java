package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.BR;
import static com.dbs.loyalty.config.constant.Constant.DELETE;
import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_ID_NOT_FOUND;
import static com.dbs.loyalty.config.constant.MessageConstant.DELETE_CONSTRAINT_VIOLATION;
import static com.dbs.loyalty.config.constant.MessageConstant.TASK_IS_SAVED;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.response.BadRequestResponse;
import com.dbs.loyalty.web.response.ErrorResponse;
import com.dbs.loyalty.web.response.SuccessResponse;

public abstract class AbstractController {

	protected ResponseEntity<AbstractResponse> taskIsSavedResponse(String taskDataType, String val, String resultUrl) {
		taskDataType = MessageUtil.getMessage(taskDataType);
		String message = MessageUtil.getMessage(TASK_IS_SAVED, taskDataType, val);
		return dataIsSavedResponse(message, resultUrl);
	}
	
	protected ResponseEntity<AbstractResponse> dataIsSavedResponse(String message, String resultUrl) {
		return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(new SuccessResponse(message, resultUrl));
	}

	protected String getNotFoundMessage(String id) {
		return MessageUtil.getMessage(DATA_WITH_ID_NOT_FOUND, id);
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
		String message = ex.getLocalizedMessage();

		if(ex instanceof DataIntegrityViolationException && ex.getMessage().contains(DELETE)){
        	message = MessageUtil.getMessage(DELETE_CONSTRAINT_VIOLATION);
        }
		
		return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorResponse(message));
	}

}
