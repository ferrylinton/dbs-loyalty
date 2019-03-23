package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.BR;
import static com.dbs.loyalty.config.Constant.EMPTY;

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

	protected final String DATA_WITH_ID_NOT_FOUND = "message.dataWithIdNotFound";
	
	protected final String TASK_IS_SAVED = "message.taskIsSaved";
	
	protected final String TASK_IS_VERIFIED = "message.taskIsVerified";
	
	protected final String TASK_IS_REJECTED = "message.taskIsRejected";
	
	protected String getNotFoundMessage(String id) {
		return MessageService.getMessage(DATA_WITH_ID_NOT_FOUND, id);
	}

	protected ResponseEntity<?> badRequestResponse(BindingResult result) {
		BadRequestResponse response = new BadRequestResponse();
		String message = EMPTY;
		
		for (FieldError fieldError : result.getFieldErrors()) {
			response.getFields().add(fieldError.getField());
			message += fieldError.getDefaultMessage() + BR; 
		}

		response.setMessage(message);
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	protected ResponseEntity<?> taskIsSavedResponse(String taskDataType, String val, String resultUrl) {
		SuccessResponse response = new SuccessResponse();
		taskDataType = MessageService.getMessage(taskDataType);
		response.setMessage(MessageService.getMessage(TASK_IS_SAVED, taskDataType, val));
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
		return MessageService.getMessage(taskDto.isVerified() ? TASK_IS_VERIFIED : TASK_IS_REJECTED, args);
	}
}
