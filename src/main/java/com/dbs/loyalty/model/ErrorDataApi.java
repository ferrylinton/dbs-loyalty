package com.dbs.loyalty.model;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import com.dbs.loyalty.exception.AbstractException;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.IpUtil;
import com.dbs.loyalty.util.MessageUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Error Response
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="ErrorDataApi", description = "Error Data API")
@Setter
@Getter
public class ErrorDataApi {
	
	@ApiModelProperty(value = "Request Url", example = "http://localhost:8181/loyalty/api/airportsxxx", position = 0)
	private String url;
	
	@ApiModelProperty(value = "Request Status", example = "404", position = 1)
	private Integer status;
	
	@ApiModelProperty(value = "Error Message", example = "Resource not found", position = 1)
	private String message;
	
	@JsonIgnore
	@ApiModelProperty(hidden = true)
	private Exception exception;
	
	public ErrorDataApi(HttpServletRequest request) {
		this.exception = getException(request);
		this.url = IpUtil.getPrefixUrl(request) + (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
		this.status = getStatusCode(request);
		this.message = getErrorMessage(request);
	}

	private int getStatusCode(HttpServletRequest request) {
		status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		
		if(status == 500 && exception != null) {
			Object ex = ErrorUtil.getThrowable(exception);
			
			if(ErrorUtil.getThrowable(exception) instanceof AbstractException) {
				AbstractException abstractException = (AbstractException) ex;
				status = abstractException.getStatus();
			}
		}
		
		return status;
	}

	private String getErrorMessage(HttpServletRequest request) {
		if(status == 404) {
			message = "Resource not found";
		}else if(status == 501 && exception != null) {
			message = exception.getMessage();
		}else if(status == 500 && exception != null) {
			Object ex = ErrorUtil.getThrowable(exception);
			
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
