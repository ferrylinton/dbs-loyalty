package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.EntityConstant.PROMO_CATEGORY;
import static com.dbs.loyalty.config.constant.EntityConstant.TYPE;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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

import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.web.response.AbstractResponse;


@Controller
@RequestMapping("/task/promocategory")
public class PromoCategoryTaskController extends AbstractTaskController {

	public PromoCategoryTaskController(final TaskService taskService) {
		super(taskService);
	}
	
	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping
	public String viewTaskPromoCategories(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "madeDate");
		Page<TaskDto> page = taskService.findAll(PROMO_CATEGORY, params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/task/promocategory";
		}
		
		request.setAttribute(PAGE, page);
		request.setAttribute(TYPE, PROMO_CATEGORY);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return "task/promocategory/task-promocategory-view";
	}
	
	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping("/{id}/detail")
	public String viewTaskPromoCategoryDetail(ModelMap model, @PathVariable String id) {
		view(PROMO_CATEGORY, model, id);
		return "task/promocategory/task-promocategory-detail";
	}


	@PreAuthorize("hasRole('PROMO_CATEGORY_CK')")
	@GetMapping("/{id}")
	public String viewTaskPromoCategory(ModelMap model, @PathVariable String id) {
		view(PROMO_CATEGORY, model, id);
		return "task/promocategory/task-promocategory-form";
	}

	@PreAuthorize("hasRole('PROMO_CATEGORY_CK')")
	@PostMapping
	public @ResponseBody ResponseEntity<AbstractResponse> saveTaskPromoCategory(@ModelAttribute TaskDto taskDto){
		return save(taskDto);
	}

}
