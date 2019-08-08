package com.dbs.loyalty.util;

import org.springframework.data.domain.Sort;

import com.dbs.loyalty.config.constant.DomainConstant;

public class SortUtil {
	
	public static final Sort SORT_BY_NAME = Sort.by(DomainConstant.NAME);
	
	private SortUtil() {
		// hide constructor
	}
	
}
