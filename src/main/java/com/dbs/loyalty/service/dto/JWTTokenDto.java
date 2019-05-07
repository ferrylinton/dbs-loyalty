package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@ApiModel(value = "JWTTokenData", description = "Authentication's response data")
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class JWTTokenDto {

	@ApiModelProperty(value = "JWT Token", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXN0b21lcjAxQGRicy5jb20iLCJleHAiOjE1NTU0NTk3NTV9.tF4LFWK6N1hethQDXUo7jyJFwgObxjZtzQ5SJH5MUZ3j1oB4ZsyxoiVSgz9eZzFzPiMDA4LlkoO26tDJGPvGww")
	@NonNull
	private String token;

	@NonNull
    private CustomerDto customer;
   
}
