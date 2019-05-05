package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
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

import com.dbs.loyalty.domain.Departure;
import com.dbs.loyalty.service.DepartureService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class DepartureController extends AbstractPageController {

	private final DepartureService departureService;

	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE')")
	@GetMapping("/departure")
	public String departure(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "flightDate");
		Page<Departure> page = departureService.findAll(getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/departure";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "departure/departure-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE')")
	@GetMapping("/departure/{id}")
	public String departure(ModelMap model, @PathVariable String id){
		Optional<Departure> current = departureService.findById(id);

		if (current.isPresent()) {
			model.addAttribute(ARRIVAL, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
		return "departure/departure-detail";
	}
	
}
