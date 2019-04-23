package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.PROMO_CATEGORY;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.validator.PromoCategoryValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/promocategory")
public class PromoCategoryController extends AbstractPageController {

	private final PromoCategoryService promoCategoryService;

	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping
	public String viewPromoCategories(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "name");
		Page<PromoCategory> page = promoCategoryService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/promocategory";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "promocategory/promocategory-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping("/{id}/detail")
	public String viewPromoCategoryDetail(ModelMap model, @PathVariable String id){
		getById(model, id);		
		return "promocategory/promocategory-detail";
	}

	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK')")
	@GetMapping("/{id}")
	public String viewPromoCategoryForm(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(PROMO_CATEGORY, new PromoCategory());
		} else {
			getById(model, id);
		}
		
		return "promocategory/promocategory-form";
	}

	@PreAuthorize("hasRole('PROMO_CATEGORY_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<AbstractResponse> save(@ModelAttribute @Valid PromoCategory promoCategory, BindingResult result) throws BadRequestException, JsonProcessingException, NotFoundException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		}
		
		if(promoCategory.getId() == null) {
			taskService.saveTaskAdd(PROMO_CATEGORY, promoCategory);
		} else {
			Optional<PromoCategory> current = promoCategoryService.findById(promoCategory.getId());
			
			if(current.isPresent()) {
				taskService.saveTaskModify(PROMO_CATEGORY, current.get(), promoCategory);
			}else {
				throw new NotFoundException();
			}
		}
		
		return taskIsSavedResponse(PROMO_CATEGORY, promoCategory.getName(), UrlUtil.getUrl(PROMO_CATEGORY));
	}

	@PreAuthorize("hasRole('PROMO_CATEGORY_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<AbstractResponse> delete(@PathVariable String id) throws JsonProcessingException, NotFoundException{
		Optional<PromoCategory> current = promoCategoryService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(PROMO_CATEGORY, current.get());
			return taskIsSavedResponse(PROMO_CATEGORY, current.get().getName(), UrlUtil.getUrl(PROMO_CATEGORY));
		}else {
			throw new NotFoundException();
		}
	}

	@InitBinder("promoCategory")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PromoCategoryValidator(promoCategoryService));
	}
	
	public void getById(ModelMap model, String id){
		Optional<PromoCategory> current = promoCategoryService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(PROMO_CATEGORY, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}

}
