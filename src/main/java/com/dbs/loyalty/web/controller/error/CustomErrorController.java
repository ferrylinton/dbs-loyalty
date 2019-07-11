package com.dbs.loyalty.web.controller.error;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CustomErrorController extends AbstractErrorController implements ErrorController {

	public CustomErrorController(final ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@GetMapping("/error")
	public String handleError(HttpServletRequest request, HttpServletResponse response) {
		return super.handleApiOrWebError(request, response);
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}

}

