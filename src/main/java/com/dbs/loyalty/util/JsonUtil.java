package com.dbs.loyalty.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtil {
	
	private static ObjectMapper objectMapper;

	public static void setObjectMapper(ObjectMapper objectMapper) {
		JsonUtil.objectMapper = objectMapper;
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}
	
	public static String toString(Object value) throws JsonProcessingException {
		return objectMapper.writeValueAsString(value);
	}
	
	public static <T> T toObject(String content, Class<T> valueType) throws IOException {
		return objectMapper.readValue(content, valueType);
	}

	@SuppressWarnings("rawtypes")
	public static <T> T readValue(String content, TypeReference valueTypeRef) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(content, valueTypeRef);
	}
	
	private JsonUtil() {
		// hide constructor
	}
	
}
