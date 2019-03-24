package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.Constant.*;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.google.common.base.Joiner;

public abstract class AbstractPageController extends AbstractController{

	protected void setParamsQueryString(Map<String,String> params, HttpServletRequest request) {
		params.remove(PAGE);
		params.remove(SORT);
		removeEmpty(params);
		request.setAttribute(PARAMS, (params.isEmpty()) ? EMPTY : AND + Joiner.on(AND).withKeyValueSeparator(EQUALS).join(params));
	}
	
	protected void removeEmpty(Map<String,String> params) {
		Map<String,String> paramsClone = new HashMap<>();
		paramsClone.putAll(params);
		for(Map.Entry<String, String> entry : paramsClone.entrySet()) {
			if(EMPTY.equals(entry.getValue())) {
				params.remove(entry.getKey());
			}
		}
	}
	
	protected void setPagerQueryString(Order order, int pageNumber, HttpServletRequest request) {
		request.setAttribute(ORDER, order);
		request.setAttribute(NEXT, getPagerQueryString(order, pageNumber + 1));
		request.setAttribute(PREVIOUS, getPagerQueryString(order, pageNumber - 1));
	}
	
	protected String getPagerQueryString(Order order, int page) {
		Map<String,String> params = new HashMap<>();
		params.put(PAGE, Integer.toString(page));
		params.put(SORT, order.getProperty() + COMMA + order.getDirection().name());
		return Joiner.on(AND).withKeyValueSeparator(EQUALS).join(params);
	}

	protected Pageable getPageable(Map<String, String> params, Order order) {
		Sort sort = Sort.by(order.ignoreCase());
		int pageNumber = params.containsKey(PAGE) ? Integer.parseInt(params.get(PAGE)) : 0 ;
		return PageRequest.of(pageNumber, SIZE, sort);
	}

}
