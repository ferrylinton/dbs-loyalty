package com.dbs.loyalty.service.specification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.LogToken;

public class LogTokenSpecification {

	public static Specification<LogToken> getSpec(HttpServletRequest request) {
		Specification<LogToken> spec = Specification.where(createdDate(request));
		
		if(request.getParameter(Constant.KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(Constant.KY_PARAM).trim())) {
			String keyword = String.format(Constant.LIKE_FORMAT, request.getParameter(Constant.KY_PARAM).trim().toLowerCase());
			return (logToken, cq, cb) -> cb.or(
						cb.like(cb.lower(logToken.get(DomainConstant.ID)), keyword),
						cb.like(cb.lower(logToken.get(DomainConstant.EMAIL)), keyword)
			);
		}
		
		return spec;
	}
	
	public static Specification<LogToken> all() {
		return (logToken, cq, cb) -> cb.notEqual(logToken.get(DomainConstant.ID), Constant.EMPTY);
	}
	
	public static Specification<LogToken> createdDate(HttpServletRequest request) {
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
	
	private LogTokenSpecification() {
		// hide constructor
	}
	
}
