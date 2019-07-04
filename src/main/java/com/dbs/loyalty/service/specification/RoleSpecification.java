package com.dbs.loyalty.service.specification;

import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Role;

public class RoleSpecification {

	public static Specification<Role> getSpec(Map<String, String> params) {
		return Specification.where(keyword(params));
	}

	public static Specification<Role> keyword(Map<String, String> params) {
		if(params.get(Constant.KY_PARAM) != null && !Constant.EMPTY.equals(params.get(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			return (role, cq, cb) -> cb.like(cb.lower(role.get(DomainConstant.NAME)), keyword);
		}else {
			return null;
		}
	}
	
	private RoleSpecification() {
		// hide constructor
	}
	
}
