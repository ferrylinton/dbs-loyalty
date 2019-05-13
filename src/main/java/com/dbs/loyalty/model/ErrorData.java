package com.dbs.loyalty.model;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.dbs.loyalty.exception.AbstractException;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.IpUtil;
import com.dbs.loyalty.util.MessageUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorData {
	
	private Exception exception;
	
	private String requestURI;
	
	private int status;
	
	private String message;
	
	private String detail;
	
	public ErrorData(HttpServletRequest request) {
		this.exception = getException(request);
		this.requestURI = IpUtil.getPrefixUrl(request) + request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		this.status = getStatusCode(request);
		this.message = getErrorMessage();
		
		if(status == 500 && exception != null) {
			this.detail = ExceptionUtils.getStackTrace(exception);
		}
	}

	private int getStatusCode(HttpServletRequest request) {
		if(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE) != null) {
			status =  (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
			
			if(status == 500 && exception != null) {
				Exception ex = (Exception) ErrorUtil.getThrowable(exception);
				
				if(ex instanceof AbstractException) {
					AbstractException abstractException = (AbstractException) ex;
					status = abstractException.getStatus();
				}
			}
		}else {
			status = 405;
		}
		
		return status;
	}

	private String getErrorMessage() {
		if(status == 404) {
			message = "Resource not found";
		}else if(status == 501 && exception != null) {
			message = exception.getMessage();
		}else if(status == 500 && exception != null) {
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

	private Exception getException(HttpServletRequest request) {
		if(request.getAttribute(RequestDispatcher.ERROR_EXCEPTION) instanceof Exception) {
			return (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		}
		
		return null;
	}
	
}
