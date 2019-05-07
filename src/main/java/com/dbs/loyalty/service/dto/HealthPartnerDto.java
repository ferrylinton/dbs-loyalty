package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="HealthPartner", description = "HealthPartner's data")
@Setter
@Getter
public class HealthPartnerDto {

	@ApiModelProperty(value = "HealthPartner's id", example = "1IUe73HCc9Qfjl1sxhasNI", position = 0)
	private String id;
	
	@ApiModelProperty(value = "HealthPartner's name", example = "BIOTEST", position = 1)
	private String name;
	
	@ApiModelProperty(value = "HealthPartner's image url", example = "/api/healthpartners/image", position = 2)
	private String imageUrl;
	
}
