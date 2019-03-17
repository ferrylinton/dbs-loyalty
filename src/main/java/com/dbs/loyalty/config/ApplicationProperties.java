package com.dbs.loyalty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
 
	private Security security;
	
	private Format format;
	
	private Async async;

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public Async getAsync() {
		return async;
	}

	public void setAsync(Async async) {
		this.async = async;
	}

	public static class Security{
		
		private int loginAttemptCount = 3;
		
		private String secret = "OTE1ZWVlYTZjZWIyODY5ODQ1ZDFkYmI3ZmM1YjNmNDdkOWU3NjUyMGYwOTcwMzEzMWY5MzU1YmU0Mzk5MzUzODY2NDVkYTA1OGI5NmRiYjNkNDQ3NzBmZGVmYTFmODcyNzliNmE2MmZmN2JlZThkMmU2Y2E3M2U4YjgyNTFhNjk=";
		
		private String base64Secret = "OTE1ZWVlYTZjZWIyODY5ODQ1ZDFkYmI3ZmM1YjNmNDdkOWU3NjUyMGYwOTcwMzEzMWY5MzU1YmU0Mzk5MzUzODY2NDVkYTA1OGI5NmRiYjNkNDQ3NzBmZGVmYTFmODcyNzliNmE2MmZmN2JlZThkMmU2Y2E3M2U4YjgyNTFhNjk=";
		
		private long tokenValidityInSeconds = 86400;
		
		private long tokenValidityInSecondsForRememberMe = 2592000;

		public int getLoginAttemptCount() {
			return loginAttemptCount;
		}

		public void setLoginAttemptCount(int loginAttemptCount) {
			this.loginAttemptCount = loginAttemptCount;
		}

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public String getBase64Secret() {
			return base64Secret;
		}

		public void setBase64Secret(String base64Secret) {
			this.base64Secret = base64Secret;
		}

		public long getTokenValidityInSeconds() {
			return tokenValidityInSeconds;
		}

		public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
			this.tokenValidityInSeconds = tokenValidityInSeconds;
		}

		public long getTokenValidityInSecondsForRememberMe() {
			return tokenValidityInSecondsForRememberMe;
		}

		public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
			this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
		}
		
	}
	
	public static class Format{
		
		private String date = "dd-MM-yyyy";
	    
	    private String dateTime = "dd-MM-yyyy HH:mm";
	    
	    private String dateTimeFull = "dd-MM-yyyy HH:mm:ss.SSS";

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getDateTime() {
			return dateTime;
		}

		public void setDateTime(String dateTime) {
			this.dateTime = dateTime;
		}

		public String getDateTimeFull() {
			return dateTimeFull;
		}

		public void setDateTimeFull(String dateTimeFull) {
			this.dateTimeFull = dateTimeFull;
		}

	}

	public static class Async{
		
		private int loginAttemptCount = 3;
		
		private int corePoolSize = 5;
		
	    private int maxPoolSize = 10;
	    
	    private int queueCapacity = 1000;

		public int getLoginAttemptCount() {
			return loginAttemptCount;
		}

		public void setLoginAttemptCount(int loginAttemptCount) {
			this.loginAttemptCount = loginAttemptCount;
		}

		public int getCorePoolSize() {
			return corePoolSize;
		}

		public void setCorePoolSize(int corePoolSize) {
			this.corePoolSize = corePoolSize;
		}

		public int getMaxPoolSize() {
			return maxPoolSize;
		}

		public void setMaxPoolSize(int maxPoolSize) {
			this.maxPoolSize = maxPoolSize;
		}

		public int getQueueCapacity() {
			return queueCapacity;
		}

		public void setQueueCapacity(int queueCapacity) {
			this.queueCapacity = queueCapacity;
		}
	    
	}
	
}
