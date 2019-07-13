package com.dbs.loyalty.domain.converter;

import java.io.IOException;
import java.util.Map;

import javax.persistence.AttributeConverter;

import com.dbs.loyalty.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

	@Override
    public String convertToDatabaseColumn(Map<String, Object> obj) {
        try {
        	return ObjectMapperUtil.getObjectMapper().writeValueAsString(obj);
        } catch (final JsonProcessingException e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
 
    @Override
    public Map<String, Object> convertToEntityAttribute(String json) {
        try {
            return ObjectMapperUtil.getObjectMapper().readValue(json, Map.class);
        } catch (final IOException e) {
        	log.error(e.getLocalizedMessage(), e);
        	return null;
        }
    }

}
