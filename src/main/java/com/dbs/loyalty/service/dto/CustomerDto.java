package com.dbs.loyalty.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.enumeration.CustomerType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDto extends AbstractImageDto {
	
	private String id;

	@NotNull(message = "{validation.notnull.email}")
	@Pattern(regexp = Constant.EMAIL_REGEX, message = "{validation.pattern.email}")
    @Size(min = 5, max = 50, message = "{validation.size.email}")
	private String email;
	
	@NotNull(message = "{validation.notnull.name}")
	@Pattern(regexp = Constant.NAME_REGEX, message = "{validation.pattern.name}")
	@Size(min = 2, max = 50, message = "{validation.size.name}")
	private String name;
	
	@NotNull(message = "{validation.notnull.phone}")
	@Size(min = 6, max = 20, message = "{validation.size.phone}")
	private String phone;
	
	private CustomerType customerType;
	
	@NotNull(message = "{validation.notnull.dob}")
	private Date dob;
	
	@JsonIgnore
	@Size(min=6, max = 30, message = "{validation.size.password}")
	private String passwordPlain;

	private String passwordHash;

	private boolean activated = true;

}
