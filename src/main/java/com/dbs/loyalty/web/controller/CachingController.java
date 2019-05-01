package com.dbs.loyalty.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.service.CachingService;
import com.dbs.loyalty.web.response.Response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/cache")
public class CachingController {

    private final CachingService cachingService;
     
    @GetMapping("/clear")
    public @ResponseBody ResponseEntity<Response> clearAllCache() {
        cachingService.evict();
        return ResponseEntity
	            .status(HttpStatus.OK)
	            .body(new Response("Ok"));
    }
    
}