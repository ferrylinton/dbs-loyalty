package com.dbs.loyalty.web.controller.error;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.model.ErrorData;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
	
	private final Logger LOG		= LoggerFactory.getLogger(ErrorController.class);
	
	private final String TEMPLATE	= "error/view";
	
	private ObjectMapper objectMapper;

	public ErrorController(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	@GetMapping("/error")
	public String handleError(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ErrorData errorData = new ErrorData(request);
		System.out.println(">>> errorData.getRequestURI() : " + errorData.getRequestURI());
		
		if(errorData.getRequestURI().contains("/api")) {
			return handleApiError(errorData, response);
		}else {
			return handleWebError(errorData, request);
		}
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

	private String handleWebError(ErrorData errorData, HttpServletRequest request) {
		request.setAttribute("errorData", errorData);
		return TEMPLATE;
	}
	
	private String handleApiError(ErrorData errorData, HttpServletResponse response){
		try {
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
	        response.setStatus(errorData.getStatusCode());

			PrintWriter writer = response.getWriter();
			writer.write(objectMapper.writeValueAsString(errorData));
	        writer.flush();
	        writer.close();
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
		
		return Constant.ERROR;
	}

}

