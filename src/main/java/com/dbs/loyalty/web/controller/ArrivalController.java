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

import com.dbs.loyalty.domain.Arrival;
import com.dbs.loyalty.service.ArrivalService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ArrivalController extends AbstractPageController {

	private final ArrivalService arrivalService;

	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE')")
	@GetMapping("/arrival")
	public String arrival(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "flightDate");
		Page<Arrival> page = arrivalService.findAll(getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/arrival";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "arrival/arrival-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE')")
	@GetMapping("/arrival/{id}")
	public String arrival(ModelMap model, @PathVariable String id){
		Optional<Arrival> current = arrivalService.findById(id);

		if (current.isPresent()) {
			model.addAttribute(ARRIVAL, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
		return "arrival/arrival-detail";
	}

}
