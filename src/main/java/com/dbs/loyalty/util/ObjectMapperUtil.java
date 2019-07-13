package com.dbs.loyalty.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectMapperUtil {
	
	private static ObjectMapper objectMapper;

	public static void setObjectMapper(ObjectMapper objectMapper) {
		ObjectMapperUtil.objectMapper = objectMapper;
	}

	public static ObjectMapper getObjectMapper() {
		return ObjectMapperUtil.objectMapper;
	}

	private ObjectMapperUtil() {
		// hide constructor
	}
	
}
