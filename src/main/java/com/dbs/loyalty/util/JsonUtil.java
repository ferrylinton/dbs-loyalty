package com.dbs.loyalty.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {
	
	private static ObjectMapper objectMapper;

	public static void setObjectMapper(ObjectMapper objectMapper) {
		JsonUtil.objectMapper = objectMapper;
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	public static String toStr(Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}
	
	public static <T> T toObj(String content, Class<T> valueType) throws IOException {
		return objectMapper.readValue(content, valueType);
	}

	private JsonUtil() {
		// hide constructor
	}
	
}
