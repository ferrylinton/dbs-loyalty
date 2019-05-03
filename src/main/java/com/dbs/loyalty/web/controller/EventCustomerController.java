package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.PAGE;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dbs.loyalty.domain.EventCustomer;
import com.dbs.loyalty.service.EventCustomerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/event")
public class EventCustomerController extends AbstractPageController {
	
	private final EventCustomerService eventCustomerService;
	
	@PreAuthorize("hasAnyRole('EVENT_MK', 'EVENT_CK')")
	@GetMapping("/{id}/customer")
	public String viewEventCustomers(@RequestParam Map<String, String> params, @PathVariable String id, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "customer.name");
		Page<EventCustomer> page = eventCustomerService.findWithCustomerByEventId(id, getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/event" + id + "/customer";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "event/event-customer";
		}
	}
	
}
