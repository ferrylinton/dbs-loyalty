package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.constant.Constant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerPasswordDto {

	@NotNull(message = "{validation.notnull.email}")
	@Pattern(regexp = Constant.EMAIL_REGEX, message = "{validation.pattern.email}")
    @Size(min = 5, max = 50, message = "{validation.size.email}")
	private String email;
	
	@NotNull(message = "{validation.notnull.oldPassword}")
	@Size(min=6, max = 30, message = "{validation.size.password}")
	private String oldPassword;
	
	@NotNull(message = "{validation.notnull.newPassword}")
	@Size(min=6, max = 30, message = "{validation.size.password}")
	private String newPassword;
	
}
