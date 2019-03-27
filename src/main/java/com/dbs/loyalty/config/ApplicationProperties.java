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
	
	private Ldap ldap;

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
	
	@Getter
	@Setter
	public static class Ldap{
		
		private String domain;
		
		private String url;
		
		private String principal;
		
		private String credentials;
		
		private String base;
		
		private String searchFilter = "(&(sAMAccountName=%s@%s))";
		
		public String getSearchFilter(String username) {
			if(domain == null) {
				return String.format(searchFilter, username);
			}else {
				return String.format(searchFilter, username, domain);
			}
		}
		
	}
	
}
