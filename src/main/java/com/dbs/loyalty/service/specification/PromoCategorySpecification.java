package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.domain.PromoCategory;

public class PromoCategorySpecification {

	public static Specification<PromoCategory> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<PromoCategory> keyword(HttpServletRequest request) {
		if(request.getParameter(KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(KY_PARAM))) {
			String keyword = String.format(LIKE_FORMAT, request.getParameter(KY_PARAM).trim().toLowerCase());
			return (logLogin, cq, cb) -> cb.like(cb.lower(logLogin.get(NAME)), keyword);
		}else {
			return (logLogin, cq, cb) -> cb.notEqual(logLogin.get(ID), EMPTY);
		}
	}
	
	private PromoCategorySpecification() {
		// hide constructor
	}
	
}
