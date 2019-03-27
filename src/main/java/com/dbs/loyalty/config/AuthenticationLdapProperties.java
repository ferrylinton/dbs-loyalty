package com.dbs.loyalty.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.ldap.authenticate", ignoreUnknownFields = false)
public class AuthenticationLdapProperties {

	private String domain;
	
	private String attribute = "uid";
	
}
