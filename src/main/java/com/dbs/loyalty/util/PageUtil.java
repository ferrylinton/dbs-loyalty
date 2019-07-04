package com.dbs.loyalty.util;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.ui.Model;

import com.dbs.loyalty.config.constant.Constant;

public class PageUtil {

	public static Pageable getPageable(Map<String, String> params, Order order) {
		Sort sort = Sort.by(order.ignoreCase());
		int pageNumber = params.containsKey(Constant.PAGE) ? Integer.parseInt(params.get(Constant.PAGE)) : 0 ;
		return PageRequest.of(pageNumber, Constant.SIZE, sort);
	}

	public static Order getOrder(Sort sort, String field) {
		Order order = sort.getOrderFor(field);
		
		if(order == null) {
			order = Order.asc(field).ignoreCase();
		}
		
		return order;
	}
	
	public static void setViewAttributes(Page<?> page, Order order, Map<String, String> params, Model model) {
		model.addAttribute(Constant.PAGE, page);
		model.addAttribute(Constant.ORDER, order);
		model.addAttribute(Constant.PREVIOUS, QueryStringUtil.page(order, page.getNumber() + 1));
		model.addAttribute(Constant.NEXT, QueryStringUtil.page(order, page.getNumber() + 1));
		model.addAttribute(Constant.PARAMS, QueryStringUtil.params(params));
	}
	
	private PageUtil() {
		// hide constructor
	}
	
}
