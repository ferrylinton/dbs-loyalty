package com.dbs.loyalty.web.controller.task;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.TaskUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/taskpromocategory")
public class PromoCategoryTaskController extends AbstractTaskController {
		
	private static final String TYPE 				= PromoCategory.class.getSimpleName();

	private static final String PROMO_CATEGORY_CK 	= "PROMO_CATEGORY_CK";
	
	private static final String REDIRECT 			= "redirect:/taskpromocategory";
	
	private final PromoCategoryService promoCategoryService;
	
	private final TaskService taskService;

	@Override
	protected Optional<Task> findById(String id) {
		return taskService.findById(id);
	}
	
	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping
	public String viewTaskPromoCategories(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, MADE_DATE);
		Page<Task> page = taskService.findAll(TYPE, params, PageUtil.getPageable(params, order));
		
		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}{
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));

			model.addAttribute(Constant.OP_PARAM, TaskUtil.getTaskOperation(params));
			model.addAttribute(Constant.ST_PARAM, TaskUtil.getTaskStatus(params));
			model.addAttribute(DomainConstant.TYPE, TYPE);
			model.addAttribute(IS_CHECKER, SecurityUtil.hasAuthority(PROMO_CATEGORY_CK));
			return TASK_VIEW_TEMPLATE;
		}
	}
	
	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping("/{id}/detail")
	public String viewTaskPromoCategoryDetail(ModelMap model, @PathVariable String id) {
		view(TYPE, model, id);
		return TASK_DETAIL_TEMPLATE;
	}


	@PreAuthorize("hasRole('PROMO_CATEGORY_CK')")
	@GetMapping("/{id}")
	public String viewTaskPromoCategory(ModelMap model, @PathVariable String id) {
		view(TYPE, model, id);
		return TASK_FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('PROMO_CATEGORY_CK')")
	@PostMapping
	public String saveTaskPromoCategory(@ModelAttribute Task task, RedirectAttributes attributes){
		try {
			String val = promoCategoryService.taskConfirm(task);
			attributes.addFlashAttribute(Constant.TOAST, getMessage(task, val));
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			promoCategoryService.taskFailed(ex, task);
			attributes.addFlashAttribute(Constant.TOAST, ex.getLocalizedMessage());
		}
		
		return REDIRECT;
	}

}
