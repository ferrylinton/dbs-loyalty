package com.dbs.loyalty.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@ApiModel(value = "JWTTokenData", description = "Authentication's response data")
@RequiredArgsConstructor
@Setter
@Getter
public class JWTTokenDto {

	@ApiModelProperty(value = "JWT Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXN0b21lcjAxQGRicy5jb20iLCJleHAiOjE1NTU0NTk3NTV9.tF4LFWK6N1hethQDXUo7jyJFwgObxjZtzQ5SJH5MUZ3j1oB4ZsyxoiVSgz9eZzFzPiMDA4LlkoO26tDJGPvGww")
	private final String token;
    
	@JsonProperty("customer")
    private final CustomerDto customerDto;
   
}
