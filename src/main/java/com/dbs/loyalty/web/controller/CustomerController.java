package com.dbs.loyalty.web.controller;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
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

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.config.constant.AddressConstant;
import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Address;
import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.service.CustomerService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PageUtil;
import com.dbs.loyalty.util.QueryStringUtil;
import com.dbs.loyalty.web.customeditor.LocalDateEditor;
import com.dbs.loyalty.web.validator.CustomerValidator;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	private static final String REDIRECT 	= "redirect:/customer";
	
	private static final String VIEW 		= "customer/customer-view";
	
	private static final String DETAIL 		= "customer/customer-detail";
	
	private static final String FORM 		= "customer/customer-form";
	
	private static final String SORT_BY 	= "firstName";

	private final CustomerService customerService;

	private final ApplicationProperties applicationProperties;

	@PreAuthorize("hasAnyRole('CUSTOMER_MK', 'CUSTOMER_CK')")
	@GetMapping
	public String viewCustomers(
			@ModelAttribute(Constant.TOAST) String toast, 
			@RequestParam Map<String, String> params, 
			Sort sort, Model model) {
		
		Order order = PageUtil.getOrder(sort, SORT_BY);
		Page<Customer> page = customerService.findAll(params, PageUtil.getPageable(params, order));

		if (page.getNumber() > 0 && page.getNumber() + 1 > page.getTotalPages()) {
			return REDIRECT;
		}else {
			model.addAttribute(Constant.PAGE, page);
			model.addAttribute(Constant.ORDER, order);
			model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() - 1));
			model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
			model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
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
		if (id.equals(Constant.ZERO)) {
			Customer customer = new Customer();
			customer.setCustomerType(applicationProperties.getCustomer().getTpc());
			
			Address primary = new Address();
			primary.setLabel(AddressConstant.PRIMARY);
			customer.setPrimary(primary);
			
			Address secondary = new Address();
			primary.setLabel(AddressConstant.SECONDARY);
			customer.setPrimary(secondary);
			
			model.addAttribute(DomainConstant.CUSTOMER, customer);
		} else {
			getById(model, id);
		}
		
		return FORM;
	}
	
	@PreAuthorize("hasRole('CUSTOMER_MK')")
	@PostMapping
	public String saveCustomer(@Valid @ModelAttribute(DomainConstant.CUSTOMER) Customer customer, BindingResult result, RedirectAttributes attributes){
		if (result.hasErrors()) {
			return FORM;
		}else {
			try {
				customerService.taskSave(customer);
				attributes.addFlashAttribute(Constant.TOAST, MessageUtil.taskIsSaved(DomainConstant.CUSTOMER, customer.getFirstName()));
			} catch (Exception e) {
				attributes.addFlashAttribute(Constant.TOAST, e.getLocalizedMessage());
			}

			return REDIRECT;
		}
	}
	
	@Transactional
	@PreAuthorize("hasRole('CUSTOMER_MK')")
	@PostMapping("/delete/{id}")
	public String deleteCustomer(@PathVariable String id, RedirectAttributes attributes) throws JsonProcessingException {
		String toast = null;
		
		try {
			Customer customer = customerService.getOne(id);
			customerService.taskDelete(customer);
			toast = MessageUtil.taskIsSavedMessage(DomainConstant.CUSTOMER, customer.getFirstName());
		} catch (Exception e) {
			toast = e.getLocalizedMessage();
		}
		
		attributes.addFlashAttribute(Constant.TOAST, toast);
		return REDIRECT;
	}

	@InitBinder(DomainConstant.CUSTOMER)
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new CustomerValidator(customerService, applicationProperties));
		binder.registerCustomEditor(LocalDate.class, new LocalDateEditor());
	}

	private void getById(ModelMap model, String id){
		Optional<Customer> customer = customerService.findById(id);
		
		if (customer.isPresent()) {
			prepareAddress(customer.get());
			model.addAttribute(DomainConstant.CUSTOMER, customer.get());
		} else {
			model.addAttribute(Constant.ERROR, MessageUtil.getNotFoundMessage(id));
		}
	}
	
	private void prepareAddress(Customer customer) {
		for(Address address: customer.getAddresses()) {
			if(address.getLabel().equals(AddressConstant.PRIMARY)) {
				customer.setPrimary(address);
			}else if(address.getLabel().equals(AddressConstant.SECONDARY)) {
				customer.setSecondary(address);
			}
		}
		
		customer.setAddresses(null);
	}
}
