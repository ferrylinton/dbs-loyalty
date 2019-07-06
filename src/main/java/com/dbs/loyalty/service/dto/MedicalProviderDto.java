package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="MedicalProvider", description = "MedicalProvider's data")
@Setter
@Getter
public class MedicalProviderDto {

	@ApiModelProperty(value = "MedicalProvider's id", example = "1IUe73HCc9Qfjl1sxhasNI", position = 0)
	private String id;
	
	@ApiModelProperty(value = "MedicalProvider's name", example = "BIOTEST", position = 1)
	private String name;

}
