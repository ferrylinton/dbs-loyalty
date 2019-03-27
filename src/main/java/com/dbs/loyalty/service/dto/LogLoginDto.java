package com.dbs.loyalty.service.dto;

import java.time.Instant;

import com.dbs.loyalty.domain.enumeration.LoginStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogLoginDto {

	private String id;
	
	private String username;
	
	private String ip;
	
	private Instant createdDate;
	
	private LoginStatus loginStatus;
	
	private String browser;
	
	private String browserType;
	
	private String browserMajorVersion;
	
	private String deviceType;
	
	private String platform;
	
	private String platformVersion;
}
