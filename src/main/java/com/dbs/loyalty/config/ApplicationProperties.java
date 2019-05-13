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

	private Async async;
	
	private Scheduler scheduler;

	@Getter
	@Setter
	public static class Security{
		
		private int maxAttempt = 3;

		private String secret;
		
		private long tokenValidityInSeconds;
		
		private long tokenValidityInSecondsForRememberMe;
		
	}
	
	@Getter
	@Setter
	public static class Async{
		
		private int corePoolSize = 5;
		
	    private int maxPoolSize = 10;
	    
	    private int queueCapacity = 1000;
	    
	}
	
	@Getter
	@Setter
	public static class Scheduler{
		
		private String customerCron = "0 0 1 * * ?";
		
	    private String filePath = "/home/ferry/loyalty/customers.csv";
	    
	}
	
}
