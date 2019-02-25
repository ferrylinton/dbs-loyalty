package com.dbs.priviledge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security", ignoreUnknownFields = false)
public class SecurityProperties {

	private String secret = "OTE1ZWVlYTZjZWIyODY5ODQ1ZDFkYmI3ZmM1YjNmNDdkOWU3NjUyMGYwOTcwMzEzMWY5MzU1YmU0Mzk5MzUzODY2NDVkYTA1OGI5NmRiYjNkNDQ3NzBmZGVmYTFmODcyNzliNmE2MmZmN2JlZThkMmU2Y2E3M2U4YjgyNTFhNjk=";
	
	private String base64Secret = "OTE1ZWVlYTZjZWIyODY5ODQ1ZDFkYmI3ZmM1YjNmNDdkOWU3NjUyMGYwOTcwMzEzMWY5MzU1YmU0Mzk5MzUzODY2NDVkYTA1OGI5NmRiYjNkNDQ3NzBmZGVmYTFmODcyNzliNmE2MmZmN2JlZThkMmU2Y2E3M2U4YjgyNTFhNjk=";
	
	private long tokenValidityInSeconds = 86400;
	
	private long tokenValidityInSecondsForRememberMe = 2592000;

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
