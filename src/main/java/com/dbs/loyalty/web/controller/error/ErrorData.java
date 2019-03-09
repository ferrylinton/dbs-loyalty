package com.dbs.loyalty.web.controller.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.dbs.loyalty.exception.AbstractException;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.util.ErrorUtil;

public class ErrorData {
	
	private final String MESSAGE_404	= "Resource not found";
	
	private Exception exception;
	
	private String requestURI;
	
	private int statusCode;
	
	private String message;
	
	private String detail;
	
	public ErrorData(HttpServletRequest request) {
		this.exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		this.requestURI = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		this.statusCode = getStatusCode(exception, request);
	}

	private int getStatusCode(Exception exception, HttpServletRequest request) {
		int statusCode =  (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if(statusCode == 500 && exception != null) {
			Exception ex = (Exception) ErrorUtil.getThrowable(exception);
			
			if(ex instanceof AbstractException) {
				AbstractException abstractException = (AbstractException) ex;
				statusCode = abstractException.getStatus();
			}
		}
		
		return statusCode;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		if(statusCode == 404) {
			message = MESSAGE_404;
		}else if(statusCode == 501) {
			message = exception.getMessage();
		}else if(statusCode == 500 && exception != null) {
			Exception ex = (Exception) ErrorUtil.getThrowable(exception);
			
			if(ex instanceof AbstractException) {
				AbstractException abstractException = (AbstractException) ex;
				message = MessageService.getMessage(abstractException.getMessage());
			}else {
				message = exception.getMessage();
			}
		}
		
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetail() {
		if(statusCode == 500 && exception != null) {
			this.detail = ExceptionUtils.getStackTrace(exception);
		}
		
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
	
}
