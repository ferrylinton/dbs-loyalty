package com.dbs.loyalty.service.specification;


import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.LogApi;

public class LogApiSpecification {

	public static Specification<LogApi> getSpec(HttpServletRequest request) {
		Specification<LogApi> spec = Specification.where(createdDate(request));
		
		if(request.getParameter(Constant.KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(Constant.KY_PARAM).trim())) {
			String keyword = String.format(Constant.LIKE_FORMAT, request.getParameter(Constant.KY_PARAM).trim().toLowerCase());
			return (logApi, cq, cb) -> cb.or(
						cb.like(cb.lower(logApi.get(DomainConstant.ID)), keyword),
						cb.like(cb.lower(logApi.get(DomainConstant.REQUEST_URI)), keyword)
			);
		}
		
		return spec;
	}
	
	public static Specification<LogApi> all() {
		return (logApi, cq, cb) -> cb.notEqual(logApi.get(DomainConstant.ID), Constant.EMPTY);
	}
	
	public static Specification<LogApi> createdDate(HttpServletRequest request) {
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
	
	private LogApiSpecification() {
		// hide constructor
	}
	
}
