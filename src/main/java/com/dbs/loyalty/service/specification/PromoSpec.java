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
import com.dbs.loyalty.domain.prm.Promo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PromoSpec implements Specification<Promo>{

private static final long serialVersionUID = 1L;
	
	private final Map<String, String> params;
	
	@Override
	public Predicate toPredicate(Root<Promo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		
		if(params.containsKey(Constant.KY_PARAM) && !Constant.EMPTY.equals(params.get(Constant.KY_PARAM))) {
			String keyword = String.format(Constant.LIKE_FORMAT, params.get(Constant.KY_PARAM).trim().toLowerCase());
			predicates.add(cb.or(
					cb.like(cb.lower(root.get(DomainConstant.CODE)), keyword),
					cb.like(cb.lower(root.get(DomainConstant.TITLE)), keyword),
					cb.like(cb.lower(root.get(DomainConstant.DESCRIPTION)), keyword)
				));
		}
		
		return cb.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
