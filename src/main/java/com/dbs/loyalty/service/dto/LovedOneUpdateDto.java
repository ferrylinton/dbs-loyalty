package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="LovedOneUpdateData", description = "Loved One's new data")
public class LovedOneUpdateDto extends LovedOneAddDto {

	@ApiModelProperty(value = "Customer Loved One's id", example = "1ag4pm80QXDfiECcxVmvw3", position = 0)
	@NotNull
	@Size(min = 36, max = 36)
	private String id;
	
}
