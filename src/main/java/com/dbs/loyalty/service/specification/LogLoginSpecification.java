package com.dbs.loyalty.service.specification;


import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.LogLogin;


public class LogLoginSpecification {

	public static Specification<LogLogin> getSpec(HttpServletRequest request) {
		Specification<LogLogin> spec = Specification.where(createdDate(request));
		
		if(request.getParameter(Constant.KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(Constant.KY_PARAM).trim())) {
			String keyword = String.format(Constant.LIKE_FORMAT, request.getParameter(Constant.KY_PARAM).trim().toLowerCase());
			return (logLogin, cq, cb) -> cb.or(
						cb.like(cb.lower(logLogin.get(DomainConstant.USERNAME)), keyword),
						cb.like(cb.lower(logLogin.get(DomainConstant.IP)), keyword),
						cb.like(cb.lower(logLogin.get(DomainConstant.BROWSER)), keyword),
						cb.like(cb.lower(logLogin.get(DomainConstant.DEVICE_TYPE)), keyword)
			);
		}
		
		return spec;
	}
	
	public static Specification<LogLogin> all() {
		return (logLogin, cq, cb) -> cb.notEqual(logLogin.get(DomainConstant.ID), Constant.EMPTY);
	}
	
	public static Specification<LogLogin> createdDate(HttpServletRequest request) {
		if(
			request.getParameter(Constant.START_DATE_PARAM) != null &&
			request.getParameter(Constant.END_DATE_PARAM) != null &&
			!request.getParameter(Constant.START_DATE_PARAM).equals(Constant.EMPTY) &&
			!request.getParameter(Constant.END_DATE_PARAM).equals(Constant.EMPTY)
			) {

			return (logLogin, cq, cb) -> cb.and(
				cb.greaterThanOrEqualTo(logLogin.get(DomainConstant.CREATED_DATE), DateSpecification.getStartDate(request)),
				cb.lessThanOrEqualTo(logLogin.get(DomainConstant.CREATED_DATE), DateSpecification.getEndDate(request))
			);
		}else {
			return all();
		}		
	}
	
	private LogLoginSpecification() {
		// hide constructor
	}
	
}
