package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.ERROR;
import static com.dbs.loyalty.config.Constant.PAGE;
import static com.dbs.loyalty.config.Constant.ZERO;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.validator.PromoValidator;

@Controller
@RequestMapping("/promos")
public class PromoController extends AbstractController {

	private final Logger LOG 				= LoggerFactory.getLogger(PromoController.class);

	private final String PROMO				= "promo";
	
	private final String PROMOS				= "promos";
	
	private final String REDIRECT 			= "redirect:/promos";

	private final String VIEW_TEMPLATE 		= "promos/view";

	private final String FORM_TEMPLATE 		= "promos/form";

	private final String SORT_BY 			= "title";
	
	private final String NAME				= "name";

	private final PromoService promoService;

	private final PromoCategoryService promoCategoryService;
	
	private final TaskService taskService;

	public PromoController(PromoService promoService, PromoCategoryService promoCategoryService, TaskService taskService) {
		this.promoService = promoService;
		this.promoCategoryService = promoCategoryService;
		this.taskService = taskService;
	}

	@PreAuthorize("hasAnyRole('PROMO', 'PROMO_VIEW')")
	@GetMapping
	public String view(HttpServletRequest request, @PageableDefault Pageable pageable) {
		Page<Promo> page = null;

		try {
			page = promoService.findAll(isValid(SORT_BY, pageable), request);
			
			if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
				return REDIRECT;
			} else {
				request.setAttribute(PAGE, page);
			}
			
		} catch (IllegalArgumentException | PropertyReferenceException ex) {
			LOG.error(ex.getLocalizedMessage());
			request.setAttribute(ERROR, ex.getLocalizedMessage());
			request.setAttribute(PAGE, promoService.findAll(getPageable(SORT_BY), request));
		}
		
		return VIEW_TEMPLATE;
	}

	@PreAuthorize("hasAnyRole('PROMO', 'PROMO_VIEW')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(PROMO, new Promo());
		} else {
			Optional<Promo> promo = promoService.findById(id);
			
			if (promo.isPresent()) {
				model.addAttribute(PROMO, promo.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return FORM_TEMPLATE;
	}

	@PreAuthorize("hasRole('PROMO')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<?> save(@ModelAttribute @Valid Promo promo, BindingResult result) {
		try {
			if (result.hasErrors()) {
				return badRequestResponse(result);
			} else {
				if(promo.getId() == null) {
					taskService.saveTaskAdd(promo);
				}else {
					Optional<Promo> current = promoService.findById(promo.getId());
					taskService.saveTaskModify(current.get(), promo);
				}

				return taskIsSavedResponse(Promo.class.getSimpleName(), promo.getTitle(), UrlUtil.getyUrl(PROMOS));
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
			Optional<Promo> promo = promoService.findById(id);
			taskService.saveTaskDelete(promo.get());
			return taskIsSavedResponse(Promo.class.getSimpleName(), promo.get().getTitle(), UrlUtil.getyUrl(PROMOS));
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return errorResponse(ex);
		}
	}

	@ModelAttribute("promocategories")
	public List<PromoCategory> getPromoCategories() {
		return promoCategoryService.findAll(Sort.by(NAME));
	}
	
	@InitBinder("promo")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new PromoValidator(promoService));
	}

}
