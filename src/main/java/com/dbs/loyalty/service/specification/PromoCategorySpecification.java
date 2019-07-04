package com.dbs.loyalty.service.specification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.PromoCategory;

public class PromoCategorySpecification {

	public static Specification<PromoCategory> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<PromoCategory> keyword(HttpServletRequest request) {
		if(request.getParameter(Constant.KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, request.getParameter(Constant.KY_PARAM).trim().toLowerCase());
			return (logLogin, cq, cb) -> cb.like(cb.lower(logLogin.get(DomainConstant.NAME)), keyword);
		}else {
			return (logLogin, cq, cb) -> cb.notEqual(logLogin.get(DomainConstant.ID), Constant.EMPTY);
		}
	}
	
	private PromoCategorySpecification() {
		// hide constructor
	}
	
}
