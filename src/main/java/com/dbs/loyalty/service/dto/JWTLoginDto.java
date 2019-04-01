package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="JWTLoginData", description = "Authentication's request data")
@Setter
@Getter
public class JWTLoginDto {

	@ApiModelProperty(value = "Customer's email", example = "johnsmith@dbs.com", required = true, position = 0)
	@NotNull
    @Size(min = 5, max = 50)
	private String email;
	
	@ApiModelProperty(value = "Customer's password", example = "password", required = true, position = 1)
	@NotNull
    @Size(min = 6, max = 30)
	private String password;
	
	@ApiModelProperty(value = "Remember me flag", required = false, position = 2)
	private Boolean rememberMe;
	
}
