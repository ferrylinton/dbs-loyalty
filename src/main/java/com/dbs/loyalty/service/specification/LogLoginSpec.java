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
import com.dbs.loyalty.domain.LogLogin;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogLoginSpec implements Specification<LogLogin>{

	private static final long serialVersionUID = 1L;
	
	private final Map<String, String> params;
	
	@Override
	public Predicate toPredicate(Root<LogLogin> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		
		if(params.containsKey(Constant.KY_PARAM) && !Constant.EMPTY.equals(params.get(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			predicates.add(cb.or(
					cb.like(cb.lower(root.get(DomainConstant.USERNAME)), keyword),
					cb.like(cb.lower(root.get(DomainConstant.IP)), keyword),
					cb.like(cb.lower(root.get(DomainConstant.BROWSER)), keyword)
			));
		}
		
		if(params.containsKey(Constant.START_DATE_PARAM) && !params.get(Constant.START_DATE_PARAM).equals(Constant.EMPTY)) {
			predicates.add(cb.greaterThanOrEqualTo(root.get(DomainConstant.CREATED_DATE), DateSpecification.getStartDate(params.get(Constant.START_DATE_PARAM))));
		}
		
		if(params.containsKey(Constant.END_DATE_PARAM) && !params.get(Constant.END_DATE_PARAM).equals(Constant.EMPTY)) {
			predicates.add(cb.lessThanOrEqualTo(root.get(DomainConstant.CREATED_DATE), DateSpecification.getEndDate(params.get(Constant.END_DATE_PARAM))));
		}
		
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}
	
}
