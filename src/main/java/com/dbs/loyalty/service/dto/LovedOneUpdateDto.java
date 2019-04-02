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

	@ApiModelProperty(value = "Customer Loved One's id", example = "646e8a2a-4ca4-459a-9da8-2a31daaecd38", position = 0)
	@NotNull(message = "{validation.notnull.id}")
	@Size(min = 36, max = 36, message = "{validation.size.id}")
	private String id;
	
}
