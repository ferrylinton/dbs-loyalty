package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.EntityConstant.APPOINTMENT;

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

import com.dbs.loyalty.domain.Appointment;
import com.dbs.loyalty.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AppointmentController extends AbstractPageController {

	private final AppointmentService appointmentService;

	@PreAuthorize("hasAnyRole('WELLNESS')")
	@GetMapping("/appointment")
	public String appointment(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "arrivalDate");
		Page<Appointment> page = appointmentService.findAll(getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/appointment";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "appointment/appointment-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('WELLNESS')")
	@GetMapping("/appointment/{id}")
	public String appointment(ModelMap model, @PathVariable String id){
		Optional<Appointment> current = appointmentService.findById(id);

		if (current.isPresent()) {
			model.addAttribute(APPOINTMENT, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
		return "appointment/appointment-detail";
	}

}
