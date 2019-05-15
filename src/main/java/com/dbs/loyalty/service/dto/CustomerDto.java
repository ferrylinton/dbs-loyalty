package com.dbs.loyalty.service.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="Customer", description = "Customer's data")
public class CustomerDto {
	
	@ApiModelProperty(value = "Customer's id", example = "1IUe73HCc9Qfjl1sxhasNI", position = 0)
	private String id;

	@ApiModelProperty(value = "Customer's email", example = "johnsmith@dbs.com", position = 1)
	private String email;
	
	@ApiModelProperty(value = "Customer's name", example = "John Smith", position = 2)
	private String name;
	
	@ApiModelProperty(value = "Customer's phone", example = "+62 8123456789", position = 3)
	private String phone;
	
	@ApiModelProperty(value = "Customer's type", example = "TPC", position = 4)
	private String customerType;
	
	@ApiModelProperty(value = "Customer's date of birth", example = "21-01-1980", position = 5)
	private Date dob;
	
	@ApiModelProperty(value = "Customer's image url", example = "/api/customers/image", position = 6)
	private String imageUrl;

}
