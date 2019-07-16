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
import com.dbs.loyalty.domain.Arrival;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArrivalSpec implements Specification<Arrival>{

	private static final long serialVersionUID = 1L;
	
	private final Map<String, String> params;

	@Override
	public Predicate toPredicate(Root<Arrival> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		
		if(params.containsKey(Constant.KY_PARAM) && !Constant.EMPTY.equals(params.get(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			predicates.add(cb.like(root.get("customer").get("name"), keyword));
		}
		
		if(params.containsKey(Constant.START_DATE_PARAM) && !params.get(Constant.START_DATE_PARAM).equals(Constant.EMPTY)) {
			predicates.add(cb.greaterThanOrEqualTo(root.get("flightDate"), DateSpecification.getStartDate(params.get(Constant.START_DATE_PARAM))));
		}
		
		if(params.containsKey(Constant.END_DATE_PARAM) && !params.get(Constant.END_DATE_PARAM).equals(Constant.EMPTY)) {
			predicates.add(cb.lessThanOrEqualTo(root.get("flightDate"), DateSpecification.getEndDate(params.get(Constant.END_DATE_PARAM))));
		}
		
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	
}
