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
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.PromoCategoryDto;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.PromoCategoryValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/promocategory")
public class PromoCategoryController extends AbstractPageController {

	private String redirect 		= "redirect:/promocategory";

	private String viewTemplate		= "promocategory/view";

	private String formTemplate		= "promocategory/form";

	private String sortBy 			= "name";
	
	private Order defaultOrder		= Order.asc(sortBy).ignoreCase();

	private final PromoCategoryService promoCategoryService;

	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort);
		Page<PromoCategoryDto> page = promoCategoryService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return redirect;
		}
		
		request.setAttribute(PAGE, page);
		setParamsQueryString(params, request);
		setPagerQueryString(order, page.getNumber(), request);
		return viewTemplate;
	}

	@PreAuthorize("hasAnyRole('PROMO_CATEGORY_MK', 'PROMO_CATEGORY_CK')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(PROMO_CATEGORY, new PromoCategoryDto());
		} else {
			Optional<PromoCategoryDto> current = promoCategoryService.findById(id);
			
			if (current.isPresent()) {
				model.addAttribute(PROMO_CATEGORY, current.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return formTemplate;
	}

	@PreAuthorize("hasRole('PROMO_CATEGORY_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute @Valid PromoCategoryDto promoCategoryDto, BindingResult result) {
		if (result.hasErrors()) {
			return badRequestResponse(result);
		}else {
			try {
				if(promoCategoryDto.getId() == null) {
					taskService.saveTaskAdd(PROMO_CATEGORY, promoCategoryDto);
				} else {
					Optional<PromoCategoryDto> current = promoCategoryService.findById(promoCategoryDto.getId());
					
					if(current.isPresent()) {
						taskService.saveTaskModify(PROMO_CATEGORY, current.get(), promoCategoryDto);
					}else {
						throw new NotFoundException();
					}
				}
				return taskIsSavedResponse(PromoCategory.class.getSimpleName(), promoCategoryDto.getName(), UrlUtil.getUrl(PROMO_CATEGORY));
			} catch (Exception ex) {
				log.error(ex.getLocalizedMessage(), ex);
				return errorResponse(ex);
			}
		}
	}

	@PreAuthorize("hasRole('PROMO_CATEGORY_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id){
		try {
			Optional<PromoCategoryDto> current = promoCategoryService.findById(id);
			
			if(current.isPresent()) {
				taskService.saveTaskDelete(PROMO_CATEGORY, current.get());
				return taskIsSavedResponse(PROMO_CATEGORY, current.get().getName(), UrlUtil.getUrl(PROMO_CATEGORY));
			}else {
				throw new NotFoundException();
			}
			
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@InitBinder("promoCategoryDto")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PromoCategoryValidator(promoCategoryService));
	}

	private Order getOrder(Sort sort) {
		Order order = sort.getOrderFor(sortBy);
		
		if(order == null) {
			order = defaultOrder;
		}
		
		return order;
	}
	
}
