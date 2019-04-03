package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.BR;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dbs.loyalty.model.BadRequestResponse;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.model.SuccessResponse;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.dto.TaskDto;

public abstract class AbstractController {

	protected String dataWithIdNotFound = "message.dataWithIdNotFound";
	
	protected String taskIsSaved = "message.taskIsSaved";
	
	protected String taskIsVerified = "message.taskIsVerified";
	
	protected String taskIsRejected = "message.taskIsRejected";
	
	protected String getNotFoundMessage(String id) {
		return MessageService.getMessage(dataWithIdNotFound, id);
	}

	protected ResponseEntity<?> badRequestResponse(BindingResult result) {
		BadRequestResponse response = new BadRequestResponse();
		StringBuilder builder = new StringBuilder();
		
		for (FieldError fieldError : result.getFieldErrors()) {
			response.getFields().add(fieldError.getField());
			builder.append(fieldError.getDefaultMessage()).append(BR);
		}

		response.setMessage(builder.toString());
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	protected ResponseEntity<?> taskIsSavedResponse(String taskDataType, String val, String resultUrl) {
		SuccessResponse response = new SuccessResponse();
		taskDataType = MessageService.getMessage(taskDataType);
		response.setMessage(MessageService.getMessage(taskIsSaved, taskDataType, val));
		response.setResultUrl(resultUrl);
		return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(response);
	}
	
	protected ResponseEntity<?> errorResponse(Exception ex){
		return ResponseEntity
	            .status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(new ErrorResponse(ex.getLocalizedMessage()));
	}
	
	protected ResponseEntity<?> saveResponse(String message, String resultUrl) {
		SuccessResponse response = new SuccessResponse();
		response.setMessage(message);
		response.setResultUrl(resultUrl);
		return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(response);
	}
	
	protected String getMessage(TaskDto taskDto, String val) {
		Object[] args = new Object[] { MessageService.getMessage(taskDto.getTaskOperation().toString()), MessageService.getMessage(taskDto.getTaskDataType()), val };
		return MessageService.getMessage(taskDto.isVerified() ? taskIsVerified : taskIsRejected, args);
	}
}
