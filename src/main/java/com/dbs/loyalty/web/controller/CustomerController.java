package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.ZERO;
import static com.dbs.loyalty.config.constant.EntityConstant.CUSTOMER;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.service.dto.CustomerDto;
import com.dbs.loyalty.util.ImageUtil;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.AbstractResponse;
import com.dbs.loyalty.web.validator.CustomerValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractPageController{

	private final CustomerService customerService;
	
	private final TaskService taskService;
	
	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping
	public String viewCustomers(@RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, "name");
		Page<CustomerDto> page = customerService.findAll(getPageable(params, order), request);

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return "redirect:/customer";
		}else {
			request.setAttribute(PAGE, page);
			setParamsQueryString(params, request);
			setPagerQueryString(order, page.getNumber(), request);
			return "customer/customer-view";
		}
	}
	
	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping("/{id}/detail")
	public String viewCustomerDetail(ModelMap model, @PathVariable String id){
		getById(model, id);
		return "customer/customer-detail";
	}
	
	@PreAuthorize("hasRole('CUSTOMER_MK')")
	@GetMapping("/{id}")
	public String viewCustomerForm(ModelMap model, @PathVariable String id) {
		if (id.equals(ZERO)) {
			model.addAttribute(CUSTOMER, new CustomerDto());
		} else {
			getById(model, id);
		}
		
		return "customer/customer-form";
	}
	
	@PreAuthorize("hasRole('CUSTOMER_MK')")
	@PostMapping
	@ResponseBody
	public ResponseEntity<AbstractResponse> saveCustomer(@Valid @ModelAttribute CustomerDto customerDto, BindingResult result) throws BadRequestException, IOException, NotFoundException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		}

		if(customerDto.getId() == null) {
			customerDto.setPasswordHash(PasswordUtil.encode(customerDto.getPasswordPlain()));
			customerDto.setPasswordPlain(null);
			ImageUtil.setImageDto(customerDto);
			taskService.saveTaskAdd(CUSTOMER, customerDto);
		}else {
			Optional<CustomerDto> current = customerService.findWithCustomerImageById(customerDto.getId());
			
			if(current.isPresent()) { 
				if(customerDto.getImageFile().isEmpty()) {
					customerDto.setImageBytes(current.get().getImageBytes());
					customerDto.setImageContentType(current.get().getImageContentType());
					customerDto.setImageWidth(current.get().getImageWidth());
					customerDto.setImageHeight(current.get().getImageHeight());
				}else {
					ImageUtil.setImageDto(customerDto);
				}
				
				customerDto.setPasswordHash(current.get().getPasswordHash());
				taskService.saveTaskModify(CUSTOMER, current.get(), customerDto);
			}else {
				throw new NotFoundException();
			}
		}

		return taskIsSavedResponse(CUSTOMER,  customerDto.getName(), UrlUtil.getUrl(CUSTOMER));
	}
	
	@PreAuthorize("hasRole('CUSTOMER_MK')")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<AbstractResponse> deleteCustomer(@PathVariable String id) throws JsonProcessingException, NotFoundException {
		Optional<CustomerDto> current = customerService.findWithCustomerImageById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(CUSTOMER, current.get());
			return taskIsSavedResponse(CUSTOMER, current.get().getName(), UrlUtil.getUrl(CUSTOMER));
		}else {
			throw new NotFoundException();
		}
	}

	@InitBinder("customerDto")
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new CustomerValidator(customerService));
	}

	private void getById(ModelMap model, String id){
		Optional<CustomerDto> customerDto = customerService.findWithCustomerImageById(id);
		
		if (customerDto.isPresent()) {
			model.addAttribute(CUSTOMER, customerDto.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}
	
}
