package com.dbs.loyalty.service.specification;


import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.User;

public class UserSpecification {

	public static Specification<User> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<User> keyword(HttpServletRequest request) {
		if(request.getParameter(Constant.KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, request.getParameter(Constant.KY_PARAM).trim().toLowerCase());
			return (user, cq, cb) -> cb.like(cb.lower(user.get(DomainConstant.USERNAME)), keyword);
		}else {
			return (user, cq, cb) -> cb.notEqual(user.get(DomainConstant.ID), Constant.EMPTY);
		}
	}
	
	private UserSpecification() {
		// hide constructor
	}
	
}
