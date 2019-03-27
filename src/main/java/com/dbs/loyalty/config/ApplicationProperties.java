package com.dbs.loyalty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
 
	private Security security;
	
	private Format format;
	
	private Async async;

	@Getter
	@Setter
	public static class Security{
		
		private int loginAttemptCount;

		private String secret;
		
		private long tokenValidityInSeconds;
		
		private long tokenValidityInSecondsForRememberMe;
		
	}
	
	@Getter
	@Setter
	public static class Format{
		
		private String date = "dd-MM-yyyy";
	    
	    private String dateTime = "dd-MM-yyyy HH:mm";
	    
	    private String dateTimeFull = "dd-MM-yyyy HH:mm:ss.SSS";

	}

	@Getter
	@Setter
	public static class Async{
		
		private int corePoolSize = 5;
		
	    private int maxPoolSize = 10;
	    
	    private int queueCapacity = 1000;
	    
	}
	
}
