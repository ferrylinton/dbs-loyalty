package com.dbs.loyalty.util;

import com.dbs.loyalty.service.CustomerTypeService;

public class CustomerUtil {
	
	private static CustomerTypeService customerTypeService;

	public static void setCustomerTypeService(CustomerTypeService customerTypeService) {
		CustomerUtil.customerTypeService = customerTypeService;
	}
	
	public static String getCustomerType(String value){
		return CustomerUtil.customerTypeService.getCustomerType(value);
	}
	
	private CustomerUtil() {
		// hide constructor
	}
	
}
