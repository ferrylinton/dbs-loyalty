package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.TOAST;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.PROMO;

import java.io.IOException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.domain.PromoCustomer;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.PromoCustomerService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.web.validator.PromoValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/promo")
public class PromoController extends AbstractPageController {

	private static final String REDIRECT = "redirect:/promo";
	
	private static final String VIEW = "promo/promo-view";
	
	private static final String DETAIL = "promo/promo-detail";
	
	private static final String FORM = "promo/promo-form";
	
	private static final String SORT_BY = "title";
	
	private final PromoService promoService;
	
	private final ImageService imageService;

	private final PromoCategoryService promoCategoryService;
	
	private final PromoCustomerService promoCustomerService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping
	public String viewPromos(@ModelAttribute(TOAST) String toast, @RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, SORT_BY);
		Page<Promo> page = promoService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			request.setAttribute(TOAST, toast);
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return VIEW;
		}
	}
	
	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping("/{id}/customer")
	public String viewPromoCustomers(@RequestParam Map<String, String> params, @PathVariable String id, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "customer.name");
		Page<PromoCustomer> page = promoCustomerService.findWithCustomerByPromoId(id, getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/promo" + id + "/customer";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "promo/promo-customer";
		}
	}
	
	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping("/{id}/detail")
	public String viewPromoDetail(ModelMap model, @PathVariable String id){
		getById(model, id);
		return DETAIL;
	}

	@PreAuthorize("hasRole('PROMO_MK')")
	@GetMapping("/{id}")
	public String viewPromoForm(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(PROMO, new Promo());
		} else {
			getById(model, id);
		}
		
		return FORM;
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_MK')")
	@PostMapping
	public String savePromo(@Valid @ModelAttribute(PROMO) Promo promo, BindingResult result, RedirectAttributes attributes) throws IOException {
		if (result.hasErrors()) {
			return FORM;
		}else {
			if(promo.getId() == null) {
				FileImageTask fileImageTask = imageService.add(promo.getMultipartFileImage());
				promo.setImage(fileImageTask.getId());

				taskService.saveTaskAdd(PROMO, promo);
			}else {
				Optional<Promo> current = promoService.findById(promo.getId());
				
				if(current.isPresent()) {
					if(promo.getMultipartFileImage().isEmpty()) {
						promo.setImage(promo.getId());
					}else {
						FileImageTask fileImageTask = imageService.add(promo.getMultipartFileImage());
						promo.setImage(fileImageTask.getId());
					}
					
					current.get().setImage(promo.getId());
					taskService.saveTaskModify(PROMO, current.get(), promo);
					promoService.save(true, promo.getId());;
				}
			}

			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(PROMO, promo.getTitle()));
			return REDIRECT;
		}
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_MK')")
	@PostMapping("/delete/{id}")
	public String deletePromo(@PathVariable String id, RedirectAttributes attributes) throws IOException {
		Optional<Promo> current = promoService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(PROMO, current.get());
			promoService.save(true, id);
			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(PROMO, current.get().getTitle()));
		}
		
		return REDIRECT;
	}

	@ModelAttribute("promoCategories")
	public List<PromoCategory> getPromoCategories() {
		return promoCategoryService.findAll();
	}
	
	@InitBinder(PROMO)
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new PromoValidator(promoService));
	}

	private void getById(ModelMap model, String id){
		Optional<Promo> current = promoService.findById(id);
		
		if (current.isPresent()) {
			model.addAttribute(PROMO, current.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}

}
