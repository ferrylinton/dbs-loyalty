package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.EntityConstant.AIRPORT_ASSISTANCE;
import static com.dbs.loyalty.config.constant.EntityConstant.DEPARTURE;
import static com.dbs.loyalty.config.constant.EntityConstant.ARRIVAL;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.domain.AirportAssistance;
import com.dbs.loyalty.domain.enumeration.FlightType;
import com.dbs.loyalty.service.AirportAssistanceService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AirportAssistanceController extends AbstractPageController {

	private final AirportAssistanceService airportAssistanceService;

	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE_MK', 'TRAVEL_ASSISTANCE_CK')")
	@GetMapping("/departure")
	public String departure(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		return getAll(DEPARTURE, FlightType.DEPARTURE, params, sort, request);
	}
	
	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE_MK', 'TRAVEL_ASSISTANCE_CK')")
	@GetMapping("/arrival")
	public String arrival(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		return getAll(ARRIVAL, FlightType.ARRIVAL, params, sort, request);
	}
	
	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE_MK', 'TRAVEL_ASSISTANCE_CK')")
	@GetMapping("/departure/{id}")
	public String departure(ModelMap model, @PathVariable String id){
		getById(DEPARTURE, model, id);		
		return "airportassistance/airportassistance-detail";
	}
	
	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE_MK', 'TRAVEL_ASSISTANCE_CK')")
	@GetMapping("/arrival/{id}")
	public String arrival(ModelMap model, @PathVariable String id){
		getById(ARRIVAL, model, id);		
		return "airportassistance/airportassistance-detail";
	}

	private String getAll(String type, FlightType flightType,  Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "flightDate");
		Page<AirportAssistance> page = airportAssistanceService.findByFlightType(flightType, getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/airportassistance";
		}else {
			request.setAttribute("type", type);
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "airportassistance/airportassistance-view";
		}
	}
	
	private void getById(String type, ModelMap model, String id){
		Optional<AirportAssistance> current = airportAssistanceService.findById(id);
		
		model.addAttribute("type", type);
		
		if (current.isPresent()) {
			model.addAttribute(AIRPORT_ASSISTANCE, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}
	
}
