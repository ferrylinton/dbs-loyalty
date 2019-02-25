package com.dbs.priviledge.web.controller.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EchoController {

	@GetMapping("/echo")
	public Map<String, Object> get(){
		Map<String, Object> result = new HashMap<>();
		result.put("message", "echo");
		return result;
	}
	
}
