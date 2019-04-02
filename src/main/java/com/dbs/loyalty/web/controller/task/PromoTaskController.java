package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.EntityConstant.PROMO;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.TaskDto;


@Controller
@RequestMapping("/task/promo")
public class PromoTaskController extends AbstractTaskController {

	public PromoTaskController(final TaskService taskService) {
		super(taskService);
	}

	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		return super.view(PROMO, params, sort, request);
	}

	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id) throws NotFoundException {
		return super.view(PROMO, model, id);
	}

	@PreAuthorize("hasRole('PROMO_CK')")
	@PostMapping
	public @ResponseBody ResponseEntity<?> save(@ModelAttribute TaskDto taskDto){
		return super.save(taskDto);
	}

}