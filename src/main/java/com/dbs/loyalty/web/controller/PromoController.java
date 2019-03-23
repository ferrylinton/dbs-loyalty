package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.ERROR;
import static com.dbs.loyalty.config.Constant.PAGE;
import static com.dbs.loyalty.config.Constant.ZERO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
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

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.util.Base64Util;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.PromoValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/promo")
public class PromoController extends AbstractPageController {

	private String ENTITY			= "promo";
	
	private String REDIRECT 		= "redirect:/promo";

	private String VIEW_TEMPLATE 	= "promo/view";

	private String FORM_TEMPLATE 	= "promo/form";

	private String SORT_BY 			= "title";
	
	private Order ORDER				= Order.asc(SORT_BY).ignoreCase();
	
	private String NAME				= "name";

	private final PromoService promoService;

	private final PromoCategoryService promoCategoryService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('PROMO', 'PROMO_VIEW')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = (sort.getOrderFor(SORT_BY) == null) ? ORDER : sort.getOrderFor(SORT_BY);
		Page<PromoDto> page = promoService.findAll(getPageable(params, order), request);

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
			model.addAttribute(ENTITY, new Promo());
		} else {
			Optional<PromoDto> promo = promoService.findById(id);
			
			if (promo.isPresent()) {
				model.addAttribute(ENTITY, promo.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('PROMO')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute @Valid PromoDto promoDto, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				if(promoDto.getId() == null) {
					taskService.saveTaskAdd(promoDto);
				}else {
					Optional<PromoDto> current = promoService.findById(promoDto.getId());
					
					if(promoDto.getFile().isEmpty()) {
						promoDto.setImageString(Base64Util.getString(current.get().getFile().getBytes()));
					}else {
						promoDto.setImageString(Base64Util.getString(promoDto.getFile().getBytes()));
					}
					
					taskService.saveTaskModify(current.get(), promoDto);
				}

				return taskIsSavedResponse(Promo.class.getSimpleName(), promoDto.getTitle(), UrlUtil.getUrl(ENTITY));
			}
			
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@PreAuthorize("hasRole('PROMO')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> delete(@PathVariable String id){
		try {
			Optional<PromoDto> promo = promoService.findById(id);
			taskService.saveTaskDelete(promo.get());
			return taskIsSavedResponse(Promo.class.getSimpleName(), promo.get().getTitle(), UrlUtil.getUrl(ENTITY));
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@ModelAttribute("promoCategories")
	public List<PromoCategory> getPromoCategories() {
		return promoCategoryService.findAll(Sort.by(NAME));
	}
	
	@InitBinder("promo")
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new PromoValidator(promoService));
	}

}
