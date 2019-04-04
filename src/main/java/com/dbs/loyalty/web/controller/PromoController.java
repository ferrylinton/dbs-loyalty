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

import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.PromoService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.PromoCategoryDto;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.util.Base64Util;
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

	private final PromoCategoryService promoCategoryService;
	
	private final TaskService taskService;

	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping
	public String view(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "title");
		Page<PromoDto> page = promoService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/promo";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "promo/view";
		}
	}

	@PreAuthorize("hasAnyRole('PROMO_MK', 'PROMO_CK')")
	@GetMapping("/{id}")
	public String view(ModelMap model, @PathVariable String id){
		if (id.equals(ZERO)) {
			model.addAttribute(PROMO, new PromoDto());
		} else {
			Optional<PromoDto> current = promoService.findById(id);
			
			if (current.isPresent()) {
				model.addAttribute(PROMO, current.get());
			} else {
				model.addAttribute(ERROR, getNotFoundMessage(id));
			}
		}
		
		return "promo/form";
	}

	@PreAuthorize("hasRole('PROMO_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<AbstractResponse> save(@ModelAttribute @Valid PromoDto promoDto, BindingResult result) throws BadRequestException, IOException, NotFoundException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		}
		
		if(promoDto.getId() == null) {
			promoDto.setImageString(Base64Util.getString(promoDto.getFile().getBytes()));
			taskService.saveTaskAdd(PROMO, promoDto);
		}else {
			Optional<PromoDto> current = promoService.findById(promoDto.getId());
			
			if(current.isPresent()) {
				if(promoDto.getFile().isEmpty()) {
					promoDto.setImageString(Base64Util.getString(current.get().getFile().getBytes()));
				}else {
					promoDto.setImageString(Base64Util.getString(promoDto.getFile().getBytes()));
				}
				
				taskService.saveTaskModify(PROMO, current.get(), promoDto);
			}else {
				throw new NotFoundException();
			}
		}

		return taskIsSavedResponse(PROMO, promoDto.getTitle(), UrlUtil.getUrl(PROMO));
	}

	@PreAuthorize("hasRole('PROMO_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<AbstractResponse> delete(@PathVariable String id) throws JsonProcessingException, NotFoundException{
		Optional<PromoDto> current = promoService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(PROMO, current.get());
			return taskIsSavedResponse(PROMO, current.get().getTitle(), UrlUtil.getUrl(PROMO));
		}else {
			throw new NotFoundException();
		}
	}

	@ModelAttribute("promoCategories")
	public List<PromoCategoryDto> getPromoCategories() {
		return promoCategoryService.findAll();
	}
	
	@InitBinder("promoDto")
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new PromoValidator(promoService));
	}

}
