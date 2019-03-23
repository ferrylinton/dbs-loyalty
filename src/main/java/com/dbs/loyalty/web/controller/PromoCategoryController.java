package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.ERROR;
import static com.dbs.loyalty.config.Constant.PAGE;
import static com.dbs.loyalty.config.Constant.ZERO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.AuthorityService;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.PromoCategoryValidator;

@Controller
@RequestMapping("/promocategory")
public class PromoCategoryController extends AbstractPageController {

	private final Logger LOG 			= LoggerFactory.getLogger(PromoCategoryController.class);

	private final String ENTITY			= "promocategory";
	
	private final String REDIRECT 		= "redirect:/promocategory";

	private final String VIEW_TEMPLATE	= "promocategory/view";

	private final String FORM_TEMPLATE	= "promocategory/form";

	private final String SORT_BY 		= "name";
	
	private final Order ORDER			= Order.asc(SORT_BY).ignoreCase();

	private final PromoCategoryService promoCategoryService;

	private final TaskService taskService;

	public PromoCategoryController(PromoCategoryService promoCategoryService, TaskService taskService) {
		this.promoCategoryService = promoCategoryService;
		this.taskService = taskService;
	}

	@PreAuthorize("hasAnyRole('PROMO', 'PROMO_VIEW')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<PromoCategory> page = promoCategoryService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}
		
		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return VIEW_TEMPLATE;
	}

	@PreAuthorize("hasAnyRole('PROMO', 'PROMO_VIEW')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(ENTITY, new PromoCategory());
		} else {
			Optional<PromoCategory> promoCategory = promoCategoryService.findById(id);
			
			if (promoCategory.isPresent()) {
				model.addAttribute(ENTITY, promoCategory.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('PROMO')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute @Valid PromoCategory promoCategory, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				if(promoCategory.getId() == null) {
					taskService.saveTaskAdd(promoCategory);
				}else {
					Optional<PromoCategory> current = promoCategoryService.findById(promoCategory.getId());
					taskService.saveTaskModify(current.get(), promoCategory);
				}

				return taskIsSavedResponse(PromoCategory.class.getSimpleName(), promoCategory.getName(), UrlUtil.getUrl(ENTITY));
			}
			
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@PreAuthorize("hasRole('PROMO')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id){
		try {
			Optional<PromoCategory> promoCategory = promoCategoryService.findById(id);
			taskService.saveTaskDelete(promoCategory.get());
			return taskIsSavedResponse(PromoCategory.class.getSimpleName(), promoCategory.get().getName(), UrlUtil.getUrl(ENTITY));
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@InitBinder("promoCategory")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PromoCategoryValidator(promoCategoryService));
	}

}
