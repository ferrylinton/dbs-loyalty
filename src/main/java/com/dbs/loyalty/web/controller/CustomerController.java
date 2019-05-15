package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.ERROR;
import static com.dbs.loyalty.config.constant.Constant.PAGE;
import static com.dbs.loyalty.config.constant.Constant.TOAST;
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

import com.dbs.loyalty.config.constant.CustomerTypeConstant;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.TaskService;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.web.validator.CustomerValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractPageController{
	
	private static final String REDIRECT = "redirect:/customer";
	
	private static final String VIEW = "customer/customer-view";
	
	private static final String DETAIL = "customer/customer-detail";
	
	private static final String FORM = "customer/customer-form";
	
	private static final String SORT_BY = "name";
	
	private final ImageService imageService;
	
	private final CustomerService customerService;
	
	private final TaskService taskService;
	
	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping
	public String viewCustomers(@ModelAttribute(TOAST) String toast, @RequestParam Map<String, String> params, Sort sort, HttpServletRequest request) {
		Order order = getOrder(sort, SORT_BY);
		Page<Customer> page = customerService.findAll(getPageable(params, order), request);

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
	
	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping("/{id}/detail")
	public String viewCustomerDetail(ModelMap model, @PathVariable String id){
		getById(model, id);
		return DETAIL;
	}
	
	@PreAuthorize("hasRole('CUSTOMER_MK')")
	@GetMapping("/{id}")
	public String viewCustomerForm(ModelMap model, @PathVariable String id) {
		if (id.equals(ZERO)) {
			Customer customer = new Customer();
			customer.setCustomerType(CustomerTypeConstant.TPC);
			model.addAttribute(CUSTOMER, customer);
		} else {
			getById(model, id);
		}
		
		return FORM;
	}
	
	@Transactional
	@PreAuthorize("hasRole('CUSTOMER_MK')")
	@PostMapping
	public String saveCustomer(@Valid @ModelAttribute(CUSTOMER) Customer customer, BindingResult result, RedirectAttributes attributes) throws IOException{
		if (result.hasErrors()) {
			return FORM;
		}else {
			if(customer.getId() == null) {
				FileImageTask fileImageTask = imageService.add(customer.getMultipartFileImage());
				customer.setImage(fileImageTask.getId());
				customer.setPasswordHash(PasswordUtil.encode(customer.getPasswordPlain()));
				taskService.saveTaskAdd(CUSTOMER, customer);
			}else {
				Optional<Customer> current = customerService.findById(customer.getId());
				
				if(current.isPresent()) { 
					if(customer.getMultipartFileImage().isEmpty()) {
						customer.setImage(customer.getId());
					}else {
						FileImageTask fileImageTask = imageService.add(customer.getMultipartFileImage());
						customer.setImage(fileImageTask.getId());
					}
					
					customer.setPasswordHash(current.get().getPasswordHash());
					current.get().setImage(customer.getId());
					taskService.saveTaskModify(CUSTOMER, current.get(), customer);
					customerService.save(true, customer.getId());
				}
			}

			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(CUSTOMER, customer.getName()));
			System.out.println("------ redirect : " + REDIRECT);
			return REDIRECT;
		}
	}
	
	@Transactional
	@PreAuthorize("hasRole('CUSTOMER_MK')")
	@PostMapping("/delete/{id}")
	public String deleteCustomer(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		Optional<Customer> current = customerService.findById(id);
		
		if(current.isPresent()) {
			taskService.saveTaskDelete(CUSTOMER, current.get());
			customerService.save(true, id);
			attributes.addFlashAttribute(TOAST, taskIsSavedMessage(CUSTOMER, current.get().getName()));
		}
		
		return REDIRECT;
	}

	@InitBinder(CUSTOMER)
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd-MM-yyyy"), true, 10)); 
		binder.addValidators(new CustomerValidator(customerService));
	}

	private void getById(ModelMap model, String id){
		Optional<Customer> customer = customerService.findById(id);
		
		if (customer.isPresent()) {
			model.addAttribute(CUSTOMER, customer.get());
		} else {
			model.addAttribute(ERROR, getNotFoundMessage(id));
		}
	}
	
}
