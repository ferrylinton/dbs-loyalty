package com.dbs.loyalty.service.specification;


import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Customer;

public class CustomerSpecification {

	public static Specification<Customer> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<Customer> keyword(HttpServletRequest request) {
		if(request.getParameter(Constant.KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, request.getParameter(Constant.KY_PARAM).trim().toLowerCase());
			return (user, cq, cb) -> cb.or(
					cb.like(cb.lower(user.get(DomainConstant.EMAIL)), keyword),
					cb.like(cb.lower(user.get(DomainConstant.NAME)), keyword)
			);
		}else {
			return (user, cq, cb) -> cb.notEqual(user.get(DomainConstant.ID), Constant.EMPTY);
		}
	}
	
	private CustomerSpecification() {
		// hide constructor
	}
	
}
