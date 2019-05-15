package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.constant.RegexConstant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="CustomerUpdateData", description = "Customer's new data")
public class CustomerUpdateDto {

	@ApiModelProperty(value = "Customer's id", example = "646e8a2a-4ca4-459a-9da8-2a31daaecd38", position = 0, hidden = true)
	private String id;
	
	@ApiModelProperty(value = "Customer's email", example = "johnsmith@dbs.com", required = true, position = 1)
	@NotNull(message = "{validation.notnull.email}")
	@Pattern(regexp = RegexConstant.EMAIL, message = RegexConstant.EMAIL_MESSAGE)
    @Size(min = 5, max = 50, message = "{validation.size.email}")
	private String email;
	
	@ApiModelProperty(value = "Customer's name", example = "John Smith", required = true, position = 2)
	@NotNull(message = "{validation.notnull.name}")
	@Pattern(regexp = RegexConstant.NAME, message = RegexConstant.NAME_MESSAGE)
	@Size(min = 2, max = 50, message = "{validation.size.name}")
	private String name;
	
	@ApiModelProperty(value = "Customer's phone", example = "+62 8123456789", required = true, position = 3)
	@NotNull(message = "{validation.notnull.phone}")
	@Size(min = 6, max = 20, message = "{validation.size.phone}")
	private String phone;
	
}
