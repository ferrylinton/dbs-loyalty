package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="LovedOneData", description = "Loved One's new data")
public class LovedOneDto extends LovedOneUpdateDto {

	@ApiModelProperty(value = "Customer data", position = 6)
	private CustomerDto customer;
	
}
