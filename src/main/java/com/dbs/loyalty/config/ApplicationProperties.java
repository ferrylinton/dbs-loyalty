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
	
	private Mail mail;

	@Getter
	@Setter
	public static class Security{
		
		private int maxAttempt = 3;

		private String secret;
		
		private long tokenValidity = 1440;
		
		private long tokenValidityForRememberMe = 43200;
		
		private long verificationTokenValidity = 15;
		
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
	
	@Getter
	@Setter
	public static class Mail{
		
		private String from = "test@dbs.com";
		
	}
	
}
