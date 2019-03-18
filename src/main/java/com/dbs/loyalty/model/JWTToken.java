package com.dbs.loyalty.model;

import com.dbs.loyalty.domain.Customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Authentication's response data")
public class JWTToken {

	@ApiModelProperty(value = "JWT Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXN0b21lcjAxQGRicy5jb20iLCJleHAiOjE1NTU0NTk3NTV9.tF4LFWK6N1hethQDXUo7jyJFwgObxjZtzQ5SJH5MUZ3j1oB4ZsyxoiVSgz9eZzFzPiMDA4LlkoO26tDJGPvGww")
	private String token;
    
    private Customer customer;

	public JWTToken(String token, Customer customer) {
		this.token = token;
		this.customer = customer;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}
