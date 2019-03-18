package com.dbs.loyalty.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Authentication's request data")
public class Login {

	@ApiModelProperty(value = "Customer's email", example = "johnsmith@dbs.com", required = true)
	@NotNull
    @Size(min = 5, max = 50)
	private String email;
	
	@ApiModelProperty(value = "Customer's password", example = "password", required = true)
	@NotNull
    @Size(min = 6, max = 30)
	private String password;
	
	private Boolean rememberMe;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(Boolean rememberMe) {
		this.rememberMe = rememberMe;
	}
	
}
