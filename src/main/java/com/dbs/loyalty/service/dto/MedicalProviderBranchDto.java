package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="MedicalProviderBranch", description = "MedicalProviderBranch's data")
@Setter
@Getter
public class MedicalProviderBranchDto {

	@ApiModelProperty(value = "MedicalProviderBranch's id", example = "1IUe73HCc9Qfjl1sxhasNI", position = 0)
	private String id;
	
	@ApiModelProperty(value = "MedicalProviderBranch's address", example = "Jl. RA Kartini No. 78", position = 1)
	private String address;

}
