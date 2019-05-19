package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="CustomerActivateData", description = "Customer's activate data")
public class CustomerActivateDto {
	
	@ApiModelProperty(value = "Customer's password", example = "pas100", required = true, position = 0)
	@NotNull
	@Size(min=6, max = 20)
	private String password;

	@ApiModelProperty(value = "Confirmation of Customer's password", example = "new100", required = true, position = 1)
	@NotNull
	@Size(min=6, max = 20)
	private String confirmPassword;
	
}
