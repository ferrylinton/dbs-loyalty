package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.BROWSER;
import static com.dbs.loyalty.service.specification.Constant.CREATED_DATE;
import static com.dbs.loyalty.service.specification.Constant.DEVICE_TYPE;
import static com.dbs.loyalty.service.specification.Constant.EMPTY;
import static com.dbs.loyalty.service.specification.Constant.END_DATE_PARAM;
import static com.dbs.loyalty.service.specification.Constant.ID;
import static com.dbs.loyalty.service.specification.Constant.IP;
import static com.dbs.loyalty.service.specification.Constant.KY_PARAM;
import static com.dbs.loyalty.service.specification.Constant.LIKE_FORMAT;
import static com.dbs.loyalty.service.specification.Constant.START_DATE_PARAM;
import static com.dbs.loyalty.service.specification.Constant.USERNAME;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.domain.LogLogin;


public class LogLoginSpecification {

	public static Specification<LogLogin> getSpec(HttpServletRequest request) {
		Specification<LogLogin> spec = Specification.where(createdDate(request));
		
		if(request.getParameter(KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(KY_PARAM).trim())) {
			String keyword = String.format(LIKE_FORMAT, request.getParameter(KY_PARAM).trim().toLowerCase());
			return (logLogin, cq, cb) -> cb.or(
						cb.like(cb.lower(logLogin.get(USERNAME)), keyword),
						cb.like(cb.lower(logLogin.get(IP)), keyword),
						cb.like(cb.lower(logLogin.get(BROWSER)), keyword),
						cb.like(cb.lower(logLogin.get(DEVICE_TYPE)), keyword)
			);
		}
		
		return spec;
	}
	
	public static Specification<LogLogin> all() {
		return (logLogin, cq, cb) -> cb.notEqual(logLogin.get(ID), EMPTY);
	}
	
	public static Specification<LogLogin> createdDate(HttpServletRequest request) {
		if(
			request.getParameter(START_DATE_PARAM) != null &&
			request.getParameter(END_DATE_PARAM) != null &&
			!request.getParameter(START_DATE_PARAM).equals(EMPTY) &&
			!request.getParameter(END_DATE_PARAM).equals(EMPTY)
			) {

			return (logLogin, cq, cb) -> cb.and(
				cb.greaterThanOrEqualTo(logLogin.get(CREATED_DATE), DateSpecification.getStartDate(request)),
				cb.lessThanOrEqualTo(logLogin.get(CREATED_DATE), DateSpecification.getEndDate(request))
			);
		}else {
			return all();
		}		
	}
	
	private LogLoginSpecification() {
		// hide constructor
	}
	
}
