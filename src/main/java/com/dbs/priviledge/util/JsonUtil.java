package com.dbs.priviledge.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

public class JsonUtil {

    private static JsonUtil instance = new JsonUtil();
    
    private ObjectMapper objectMapper = null;

    private JsonUtil() {
    }

    public static JsonUtil getInstance() {
        return instance;
    }

	public ObjectMapper getObjectMapper() {
		if(objectMapper == null){
			objectMapper = ApplicationContextProvider.getApplicationContext().getBean(ObjectMapper.class);
			Hibernate5Module hibernateModule = new Hibernate5Module();
	    	hibernateModule.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, false); 
	        
	    	objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	        objectMapper.registerModule(hibernateModule);
		}
		
		return objectMapper;
	}
	
}
