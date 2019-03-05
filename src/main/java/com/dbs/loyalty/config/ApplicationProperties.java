package com.dbs.loyalty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
 
    private String dateFormat = "dd-MM-yyyy";
    
    private String dateTimeFormat = "dd-MM-yyyy HH:mm";
    
    private String dateTimeFullFormat = "dd-MM-yyyy HH:mm:ss.SSS";

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
