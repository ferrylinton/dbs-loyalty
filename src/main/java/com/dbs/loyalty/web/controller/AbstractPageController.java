package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.Constant.EMPTY;
import static com.dbs.loyalty.config.Constant.KEYWORD;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.dbs.loyalty.config.Constant;
import com.google.common.base.Joiner;

public abstract class AbstractPageController extends AbstractController{

	protected void setParamsQueryString(Map<String,String> params, HttpServletRequest request) {
		params.remove("page");
		params.remove("sort");
		removeEmpty(params);
		request.setAttribute("params", (params.isEmpty()) ? "" : "&" + Joiner.on("&").withKeyValueSeparator("=").join(params));
	}
	
	protected void removeEmpty(Map<String,String> params) {
		Map<String,String> paramsClone = new HashMap<>();
		paramsClone.putAll(params);
		for(Map.Entry<String, String> entry : paramsClone.entrySet()) {
			if(Constant.EMPTY.equals(entry.getValue())) {
				params.remove(entry.getKey());
			}
		}
	}
	
	protected void setPagerQueryString(Page<?> page, HttpServletRequest request) {
		Order order = page.getSort().stream().collect(Collectors.toList()).get(0);
		request.setAttribute("order", order);
		request.setAttribute("next", getPagerQueryString(order, page.getNumber() + 1));
		request.setAttribute("previous", getPagerQueryString(order, page.getNumber() - 1));
	}
	
	protected String getPagerQueryString(Order order, int page) {
		Map<String,String> params = new HashMap<>();
		params.put("page", Integer.toString(page));
		params.put("sort", order.getProperty() + "," + order.getDirection().name());
		return Joiner.on("&").withKeyValueSeparator("=").join(params);
	}

	protected Pageable isValid(String sortBy, Pageable pageable) {
		List<Order> orders = pageable.getSort().stream().collect(Collectors.toList());
		if (orders.size() > 0) {
			return pageable;
		} else {
			return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortBy));
		}
	}
	
	protected Pageable getPageable(String sortBy) {
		return PageRequest.of(0, 10, Sort.by(sortBy));
	}

	protected String getKeyword(HttpServletRequest request) {
		String keyword = request.getParameter(KEYWORD);
		
		if (keyword == null || EMPTY.equals(keyword.trim())) {
			return null;
		} else {
			keyword = keyword.trim().toLowerCase();
			request.setAttribute(KEYWORD, keyword);
			return keyword;
		}
	}
}
