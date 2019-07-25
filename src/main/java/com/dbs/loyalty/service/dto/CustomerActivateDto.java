package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="CustomerActivateData", description = "Customer's activate data")
public class CustomerActivateDto {
	
	@ApiModelProperty(value = "Customer's email", example = "johnsmith@dbs.com", required = true, position = 0)
	@NotNull
    @Size(min = 5, max = 50)
	private String email;

	@ApiModelProperty(value = "Password", example = "pas100", required = true, position = 1)
	@JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	@Size(min=6, max = 20)
	private String password;

	@ApiModelProperty(value = "Confirm password", example = "new100", required = true, position = 2)
	@JsonProperty( value = "confirmPassword", access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	@Size(min=6, max = 20)
	private String confirmPassword;
	
}
