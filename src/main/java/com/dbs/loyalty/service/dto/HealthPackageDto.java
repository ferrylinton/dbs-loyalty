package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="HealthPackage", description = "HealthPackage's data")
@Setter
@Getter
public class HealthPackageDto {

	@ApiModelProperty(value = "HealthPackage's id", example = "1IUe73HCc9Qfjl1sxhasNI", position = 0)
	private String id;
	
	@ApiModelProperty(value = "HealthPackage's name", example = "Package 1", position = 1)
	private String name;
	
	@ApiModelProperty(value = "HealthPackage's content", example = "HealthPackage's description", position = 2)
	private String contentUrl;
	
}
