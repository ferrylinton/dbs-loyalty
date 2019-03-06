package com.dbs.loyalty.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.model.BadRequestResponse;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.model.SuccessResponse;
import com.dbs.loyalty.service.MessageService;

public abstract class AbstractController {

	private final String DATA_WITH_ID_NOT_FOUND = "message.dataWithIdNotFound";
	
	private final String TASK_IS_SAVED = "message.taskIsSaved";
	
	protected Pageable isValid(String sortBy, Pageable pageable) {
		List<Order> orders = pageable.getSort().stream().collect(Collectors.toList());
		if (orders.size() > 0) {
			return pageable;
		} else {
			return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortBy));
		}
	}
	
	protected Pageable getPageable(String sortBy) {
		return PageRequest.of(0, 10, Sort.by(sortBy));
	}

	protected String getKeyword(HttpServletRequest request) {
		String keyword = request.getParameter(Constant.KEYWORD);
		if (keyword == null) {
			return Constant.EMPTY;
		} else {
			keyword = keyword.trim().toLowerCase();
			request.setAttribute(Constant.KEYWORD, keyword);
			return keyword;
		}
	}
	
	protected String getNotFoundMessage(String id) {
		return MessageService.getMessage(DATA_WITH_ID_NOT_FOUND, id);
	}

	protected ResponseEntity<?> badRequestResponse(BindingResult result) {
		BadRequestResponse response = new BadRequestResponse();
		String message = Constant.EMPTY;
		
		for (FieldError fieldError : result.getFieldErrors()) {
			response.getFields().add(fieldError.getField());
			message += fieldError.getDefaultMessage() + Constant.BR; 
		}

		response.setMessage(message);
		return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
	protected ResponseEntity<?> taskIsSavedResponse(String taskDataType, String val, String resultUrl) {
		SuccessResponse response = new SuccessResponse();
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
}
