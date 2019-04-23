package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.domain.Promo;
import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.validator.PromoValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/promo")
public class PromoController extends AbstractPageController {

	private final PromoService promoService;
	
	private final ImageService imageService;

	private final PromoCategoryService promoCategoryService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping
	public String viewPromos(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "title");
		Page<Promo> page = promoService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/promo";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "promo/promo-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping("/{id}/detail")
	public String viewPromoDetail(ModelMap model, @PathVariable String id){
		getById(model, id);
		return "promo/promo-detail";
	}

	@PreAuthorize("hasRole('PROMO_MK')")
	@GetMapping("/{id}")
	public String viewPromoForm(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(PROMO, new Promo());
		} else {
			getById(model, id);
		}
		
		return "promo/promo-form";
	}

	@Transactional
	@PreAuthorize("hasRole('PROMO_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<AbstractResponse> savePromo(@ModelAttribute @Valid Promo promo, BindingResult result) throws BadRequestException, IOException, NotFoundException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		}
		
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
			}else {
				throw new NotFoundException();
			}
		}

		return taskIsSavedResponse(PROMO, promo.getTitle(), UrlUtil.getUrl(PROMO));
	}

	@PreAuthorize("hasRole('PROMO_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<AbstractResponse> deletePromo(@PathVariable String id) throws JsonProcessingException, NotFoundException{
		Optional<Promo> current = promoService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(PROMO, current.get());
			return taskIsSavedResponse(PROMO, current.get().getTitle(), UrlUtil.getUrl(PROMO));
		}else {
			throw new NotFoundException();
		}
	}

	@ModelAttribute("promoCategories")
	public List<PromoCategory> getPromoCategories() {
		return promoCategoryService.findAll();
	}
	
	@InitBinder("promo")
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
