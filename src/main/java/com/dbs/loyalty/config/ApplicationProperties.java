package com.dbs.loyalty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
 
	private int loginAttemptCount = 3;
	
	private int corePoolSize = 5;
	
    private int maxPoolSize = 10;
    
    private int queueCapacity = 1000;
    
    private String dateFormat = "dd-MM-yyyy";
    
    private String dateTimeFormat = "dd-MM-yyyy HH:mm";
    
    private String dateTimeFullFormat = "dd-MM-yyyy HH:mm:ss.SSS";

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

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(String dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}

	public String getDateTimeFullFormat() {
		return dateTimeFullFormat;
	}

	public void setDateTimeFullFormat(String dateTimeFullFormat) {
		this.dateTimeFullFormat = dateTimeFullFormat;
	}

}
