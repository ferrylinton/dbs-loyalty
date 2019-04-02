package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserPasswordDto{

	private String loggedUsername;
	
	private String username;

	@NotNull(message = "{validation.notnull.password}")
	@Size(min=6, max = 30, message = "{validation.size.password}")
	private String passwordPlain;
	
	public boolean isOwnPassword() {
		return loggedUsername.equals(username);
	}
	
}
