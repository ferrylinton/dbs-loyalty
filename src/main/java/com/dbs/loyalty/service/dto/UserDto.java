package com.dbs.loyalty.service.dto;

import com.dbs.loyalty.domain.Role;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

	private String id;
	
	private String username;
	
	private String email;
	
	private String passwordPlain;
	
	private String passwordHash;
	
	private boolean activated;
	
	private boolean locked;
	
	private Boolean authenticateFromDb;
	
	private Integer loginAttemptCount;
	
	private Role role;
	
}
