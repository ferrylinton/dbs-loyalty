package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.DESCRIPTION;
import static com.dbs.loyalty.service.specification.Constant.ID;
import static com.dbs.loyalty.service.specification.Constant.KY_PARAM;
import static com.dbs.loyalty.service.specification.Constant.TITLE;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.Event;


public class EventSpecification {

	public static Specification<Event> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<Event> keyword(HttpServletRequest request) {
		if(request.getParameter(KY_PARAM) != null) {
			String keyword = request.getParameter(KY_PARAM).trim().toLowerCase();
			return (task, cq, cb) -> cb.or(
						cb.like(cb.lower(task.get(TITLE)), keyword),
						cb.like(cb.lower(task.get(DESCRIPTION)), keyword)
					);
		}else {
			return (task, cq, cb) -> cb.notEqual(task.get(ID), Constant.EMPTY);
		}
	}
	
	private EventSpecification() {
		// hide constructor
	}
	
}
