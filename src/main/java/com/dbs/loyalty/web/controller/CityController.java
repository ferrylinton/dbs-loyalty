package com.dbs.loyalty.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.projection.NameOnly;
import com.dbs.loyalty.service.CityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/city")
public class CityController {

	private final CityService cityService;
	
	@GetMapping("/search/{name}")
	public @ResponseBody List<NameOnly> viewAirports(@PathVariable String name) {
		return cityService.findByName(name);
	}

}
