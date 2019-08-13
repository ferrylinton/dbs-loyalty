package com.dbs.loyalty.service.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.City;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CitySpec implements Specification<City>{

	private static final long serialVersionUID = 1L;
	
	private final Map<String, String> params;
	
	@Override
	public Predicate toPredicate(Root<City> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		
		if(params.containsKey(Constant.KY_PARAM) && !Constant.EMPTY.equals(params.get(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			predicates.add(cb.like(root.get(DomainConstant.ADDRESS), keyword));
		}
		
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
