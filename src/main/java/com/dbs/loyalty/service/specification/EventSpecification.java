package com.dbs.loyalty.service.specification;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Event;


public class EventSpecification {

	public static Specification<Event> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<Event> keyword(HttpServletRequest request) {
		if(request.getParameter(Constant.KY_PARAM) != null) {
			String keyword = request.getParameter(Constant.KY_PARAM).trim().toLowerCase();
			return (task, cq, cb) -> cb.or(
						cb.like(cb.lower(task.get(DomainConstant.TITLE)), keyword),
						cb.like(cb.lower(task.get(DomainConstant.DESCRIPTION)), keyword)
					);
		}else {
			return (task, cq, cb) -> cb.notEqual(task.get(DomainConstant.ID), Constant.EMPTY);
		}
	}
	
	private EventSpecification() {
		// hide constructor
	}
	
}
