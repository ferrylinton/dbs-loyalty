package com.dbs.loyalty.service.specification;

import static com.dbs.loyalty.service.specification.Constant.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.domain.Role;

public class RoleSpecification {

	public static Specification<Role> getSpec(HttpServletRequest request) {
		return Specification.where(keyword(request));
	}

	public static Specification<Role> keyword(HttpServletRequest request) {
		if(request.getParameter(KY_PARAM) != null && !Constant.EMPTY.equals(request.getParameter(KY_PARAM))) {
			String keyword = String.format(LIKE_FORMAT, request.getParameter(KY_PARAM).trim().toLowerCase());
			return (role, cq, cb) -> cb.like(cb.lower(role.get(NAME)), keyword);
		}else {
			return (role, cq, cb) -> cb.notEqual(role.get(ID), EMPTY);
		}
	}
	
}
