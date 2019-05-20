package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="CustomerNewPassword", description = "Customer's new password")
public class CustomerNewPasswordDto {
	
	@ApiModelProperty(value = "Password", example = "pas100", required = true, position = 0)
	@NotNull
	@Size(min=6, max = 20)
	private String password;

	@ApiModelProperty(value = "Confirm password", example = "new100", required = true, position = 1)
	@NotNull
	@Size(min=6, max = 20)
	private String confirmPassword;
	
}
