package com.dbs.loyalty.web.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Departure;
import com.dbs.loyalty.service.DepartureService;
import com.dbs.loyalty.util.QueryStringUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class DepartureController extends AbstractPageController {

	private static final String REDIRECT 	= "redirect:/departure";
	
	private static final String VIEW 		= "departure/departure-view";
	
	private static final String DETAIL 		= "departure/departure-detail";
	
	private static final String SORT_BY 	= "flightDate";
	
	private final DepartureService departureService;

	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE')")
	@GetMapping("/departure")
	public String departure(@ModelAttribute(Constant.TOAST) String toast, @RequestParam Map<String, String> params, Sort sort, Model model) {
		Order order = getOrder(sort, SORT_BY);
		Page<Departure> page = departureService.findAll(params, getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
			return VIEW;
		}
	}
	
	@PreAuthorize("hasAnyRole('TRAVEL_ASSISTANCE')")
	@GetMapping("/departure/{id}")
	public String departure(ModelMap model, @PathVariable String id){
		Optional<Departure> current = departureService.findById(id);

		if (current.isPresent()) {
			model.addAttribute(DomainConstant.DEPARTURE, current.get());
		} else {
			model.addAttribute(Constant.ERROR, getNotFoundMessage(id));
		}
		return DETAIL;
	}
	
}
