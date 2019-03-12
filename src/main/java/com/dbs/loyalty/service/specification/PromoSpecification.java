package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Promo;


public class PromoSpecification {

	public static Specification<Promo> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<Promo> keyword(HttpServletRequest request) {
		if(request.getParameter(KY_PARAM) != null) {
			String keyword = request.getParameter(KY_PARAM).trim().toLowerCase();
			return (task, cq, cb) -> cb.or(
						cb.like(cb.lower(task.get(CODE)), keyword),
						cb.like(cb.lower(task.get(TITLE)), keyword),
						cb.like(cb.lower(task.get(DESCRIPTION)), keyword)
					);
		}else {
			return (task, cq, cb) -> cb.notEqual(task.get(ID), Constant.EMPTY);
		}
	}
	
}
