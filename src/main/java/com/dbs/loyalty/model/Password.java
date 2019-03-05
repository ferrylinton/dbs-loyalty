package com.dbs.loyalty.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class Password{

	private String loggedEmail;
	
	private String email;

	@NotNull(message = "{validation.notnull.password}")
	@Size(min=6, max = 30, message = "{validation.size.password}")
	private String passwordPlain;

	public String getLoggedEmail() {
		return loggedEmail;
	}

	public void setLoggedEmail(String loggedEmail) {
		this.loggedEmail = loggedEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordPlain() {
		return passwordPlain;
	}

	public void setPasswordPlain(String passwordPlain) {
		this.passwordPlain = passwordPlain;
	}

	public boolean isOwnPassword() {
		return loggedEmail.equals(email);
	}
	
}
