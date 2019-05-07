package com.dbs.loyalty.model;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.dbs.loyalty.exception.AbstractException;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.IpUtil;
import com.dbs.loyalty.util.MessageUtil;

public class ErrorData {
	
	private String message404	= "Resource not found";
	
	private Exception exception;
	
	private String requestURI;
	
	private int statusCode;
	
	private String message;
	
	private String detail;
	
	public ErrorData(HttpServletRequest request) {
		if(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION) instanceof Exception) {
			this.exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		}
		this.requestURI = IpUtil.getPrefixUrl(request) + (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		this.statusCode = getStatusCode(request);
	}

	private int getStatusCode(HttpServletRequest request) {
		if(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION) instanceof Exception) {
			statusCode =  (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			
			if(statusCode == 500 && exception != null) {
				Exception ex = (Exception) ErrorUtil.getThrowable(exception);
				
				if(ex instanceof AbstractException) {
					AbstractException abstractException = (AbstractException) ex;
					statusCode = abstractException.getStatus();
				}
			}
			
			return statusCode;
		}else {
			return 500;
		}		
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
			message = message404;
		}else if(statusCode == 501) {
			message = exception.getMessage();
		}else if(statusCode == 500 && exception != null) {
			Exception ex = (Exception) ErrorUtil.getThrowable(exception);
			
			if(ex instanceof AbstractException) {
				AbstractException abstractException = (AbstractException) ex;
				message = MessageUtil.getMessage(abstractException.getMessage());
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
