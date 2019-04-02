package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.domain.Customer;

public class CustomerSpecification {

	public static Specification<Customer> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<Customer> keyword(HttpServletRequest request) {
		if(request.getParameter(KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(KY_PARAM))) {
			String keyword = String.format(LIKE_FORMAT, request.getParameter(KY_PARAM).trim().toLowerCase());
			return (user, cq, cb) -> cb.or(
					cb.like(cb.lower(user.get(EMAIL)), keyword),
					cb.like(cb.lower(user.get(NAME)), keyword)
			);
		}else {
			return (user, cq, cb) -> cb.notEqual(user.get(ID), EMPTY);
		}
	}
	
	private CustomerSpecification() {
		// hide constructor
	}
	
}
