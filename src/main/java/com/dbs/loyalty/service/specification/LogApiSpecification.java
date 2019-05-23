package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.CREATED_DATE;
import static com.dbs.loyalty.service.specification.Constant.EMPTY;
import static com.dbs.loyalty.service.specification.Constant.END_DATE_PARAM;
import static com.dbs.loyalty.service.specification.Constant.ID;
import static com.dbs.loyalty.service.specification.Constant.KY_PARAM;
import static com.dbs.loyalty.service.specification.Constant.LIKE_FORMAT;
import static com.dbs.loyalty.service.specification.Constant.REQUEST_URI;
import static com.dbs.loyalty.service.specification.Constant.START_DATE_PARAM;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.domain.LogApi;

public class LogApiSpecification {

	public static Specification<LogApi> getSpec(HttpServletRequest request) {
		Specification<LogApi> spec = Specification.where(createdDate(request));
		
		if(request.getParameter(KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(KY_PARAM).trim())) {
			String keyword = String.format(LIKE_FORMAT, request.getParameter(KY_PARAM).trim().toLowerCase());
			return (logApi, cq, cb) -> cb.or(
						cb.like(cb.lower(logApi.get(ID)), keyword),
						cb.like(cb.lower(logApi.get(REQUEST_URI)), keyword)
			);
		}
		
		return spec;
	}
	
	public static Specification<LogApi> all() {
		return (logApi, cq, cb) -> cb.notEqual(logApi.get(ID), EMPTY);
	}
	
	public static Specification<LogApi> createdDate(HttpServletRequest request) {
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
	
	private LogApiSpecification() {
		// hide constructor
	}
	
}
