package com.dbs.loyalty.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.dbs.loyalty.config.Constant;

public abstract class AbstractController {

	protected Pageable isValid(String sortBy, Pageable pageable) {
		List<Order> orders = pageable.getSort().stream().collect(Collectors.toList());
		if (orders.size() > 0) {
			return pageable;
		} else {
			return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortBy));
		}
	}

	protected String getKeyword(HttpServletRequest request) {
		String keyword = request.getParameter(Constant.KEYWORD);
		if (keyword == null) {
			return Constant.EMPTY;
		} else {
			keyword = keyword.trim().toLowerCase();
			request.setAttribute(Constant.KEYWORD, keyword);
			return keyword;
		}
	}

}
