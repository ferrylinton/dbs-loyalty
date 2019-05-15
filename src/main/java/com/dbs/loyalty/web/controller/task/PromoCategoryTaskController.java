package com.dbs.loyalty.web.controller.task;

import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.TOAST;
import static com.dbs.loyalty.config.constant.EntityConstant.PROMO_CATEGORY;
import static com.dbs.loyalty.config.constant.EntityConstant.TYPE;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.SecurityUtil;


@Controller
@RequestMapping("/taskpromocategory")
public class PromoCategoryTaskController extends AbstractTaskController {

	private static final String PROMO_CATEGORY_CK = "PROMO_CATEGORY_CK";
	
	private static final String REDIRECT = "redirect:/taskpromocategory";
	
	public PromoCategoryTaskController(final TaskService taskService) {
		super(taskService);
	}
	
	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping
	public String viewTaskPromoCategories(@ModelAttribute(TOAST) String toast, @RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, MADE_DATE);
		Page<Task> page = taskService.findAll(PROMO_CATEGORY, params, getPageable(params, order), request);
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}
		
		request.setAttribute(TOAST, toast);
		request.setAttribute(PAGE, page);
		request.setAttribute(TYPE, PROMO_CATEGORY);
		request.setAttribute(IS_CHECKER, SecurityUtil.hasAuthority(PROMO_CATEGORY_CK));
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return TASK_VIEW_TEMPLATE;
	}
	
	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping("/{id}/detail")
	public String viewTaskPromoCategoryDetail(ModelMap model, @PathVariable String id) {
		view(PROMO_CATEGORY, model, id);
		return TASK_DETAIL_TEMPLATE;
	}


	@PreAuthorize("hasRole('PROMO_CATEGORY_CK')")
	@GetMapping("/{id}")
	public String viewTaskPromoCategory(ModelMap model, @PathVariable String id) {
		view(PROMO_CATEGORY, model, id);
		return TASK_FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('PROMO_CATEGORY_CK')")
	@PostMapping
	public String saveTaskPromoCategory(@ModelAttribute Task task, RedirectAttributes attributes){
		attributes.addFlashAttribute(TOAST, save(task));
		return REDIRECT;
	}

}
