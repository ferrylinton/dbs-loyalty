package com.dbs.priviledge.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class Password{

	private String loggedUsername;
	
	private String username;

	@NotNull(message = "{validation.notnull.password}")
	@Size(min=6, max = 30, message = "{validation.size.password}")
	private String passwordPlain;

	public String getLoggedUsername() {
		return loggedUsername;
	}

	public void setLoggedUsername(String loggedUsername) {
		this.loggedUsername = loggedUsername;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordPlain() {
		return passwordPlain;
	}

	public void setPasswordPlain(String passwordPlain) {
		this.passwordPlain = passwordPlain;
	}

	public boolean isOwnPassword() {
		return loggedUsername.equals(username);
	}
	
}
