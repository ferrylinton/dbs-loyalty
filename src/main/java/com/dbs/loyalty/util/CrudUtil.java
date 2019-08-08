package com.dbs.loyalty.util;

import java.time.Instant;

import com.dbs.loyalty.domain.AbstractAuditing;

public class CrudUtil {
	
	public static <T extends AbstractAuditing> void setCreatedData(T t) {
		t.setCreatedBy(SecurityUtil.getLogged());
		t.setCreatedDate(Instant.now());
	}
	
	public static <T extends AbstractAuditing> void setModifiedData(T t) {
		t.setLastModifiedBy(SecurityUtil.getLogged());
		t.setLastModifiedDate(Instant.now());
	}
	
	private CrudUtil() {
		// hide constructor
	}
	
}
