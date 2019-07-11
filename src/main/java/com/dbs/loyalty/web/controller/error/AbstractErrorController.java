package com.dbs.loyalty.web.controller.error;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.model.ErrorData;
import com.dbs.loyalty.model.ErrorDataApi;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public abstract class AbstractErrorController{

	protected static final String VIEW = "error/error-view";
	
	protected static final String API = "/api/";
	
	protected static final String ERROR_DATA = "errorData";
	
	protected final ObjectMapper objectMapper;

	protected String handleApiOrWebError(HttpServletRequest request, HttpServletResponse response) {
		if(((String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)).contains(API)) {
			return handleApiError(new ErrorDataApi(request), response);
		}else {
			return handleWebError(new ErrorData(request), request);
		}
	}
	
	private String handleWebError(ErrorData errorData, HttpServletRequest request) {
		request.setAttribute(ERROR_DATA, errorData);
		return VIEW;
	}
	
	private String handleApiError(ErrorDataApi errorData, HttpServletResponse response){
		try {
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	        response.setStatus(errorData.getStatus());

			PrintWriter writer = response.getWriter();
			writer.write(objectMapper.writeValueAsString(errorData));
	        writer.flush();
	        writer.close();
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		
		return Constant.ERROR;
	}

}

