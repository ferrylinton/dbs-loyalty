package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto extends AbstractAuditDto {

	private String id;
	
	@NotNull(message = "{validation.notnull.username}")
	@Pattern(regexp = Constant.USERNAME_REGEX, message = "{validation.pattern.username}")
	@Size(min = 2, max = 50, message = "{validation.size.username}")
	private String username;
	
	@JsonIgnore
	private String passwordPlain;
	
	@JsonIgnore
	private String passwordHash;
	
	private boolean activated;
	
	private boolean locked;
	
	private UserType userType;
	
	@JsonIgnore
	private Integer loginAttemptCount;
	
	@JsonIgnoreProperties("authorities")
	private RoleDto role;
	
}
