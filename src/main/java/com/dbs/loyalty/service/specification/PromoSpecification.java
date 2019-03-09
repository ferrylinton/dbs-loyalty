package com.dbs.loyalty.service.specification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Promo;


public class PromoSpecification {

	public static final String CODE = "code";
	
	public static final String ID = "id";
	
	public static final String TITLE = "title";
	
	public static final String DESCRIPTION = "description";
	
	public static final String LIKE_FORMAT = "%%%s%%";

	public static final String KY_PARAM = "ky";;
	
	public static Specification<Promo> getSpecfication(HttpServletRequest request) {
		Specification<Promo> specification = Specification
				.where(all())
				.and(keyword(request));
		
		return specification;
	}
	
	public static Specification<Promo> all() {
		return (task, cq, cb) -> cb.notEqual(task.get(ID), Constant.EMPTY);
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
			return null;
		}
	}
	
}
