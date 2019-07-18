package com.dbs.loyalty.service.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.dbs.loyalty.config.constant.DateConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

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
	
	@ApiModelProperty(value = "Customer's first name", example = "John", position = 2)
	private String firstName;
	
	@ApiModelProperty(value = "Customer's last name", example = "Smith", position = 3)
	private String lastName;
	
	@ApiModelProperty(value = "Customer's phone", example = "+62 8123456789", position = 4)
	private String phone;
	
	@ApiModelProperty(value = "Customer's type", example = "TPC", position = 5)
	private String customerType;
	
	@ApiModelProperty(value = "Customer's date of birth", example = "21/01/1980", position = 6)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateConstant.JAVA_DATE)
	private LocalDate dob;
	
	@ApiModelProperty(value = "Customer's image url", example = "/api/customers/image", position = 7)
	private String imageUrl;

	@ApiModelProperty(value = "Customer's addresses", position = 8)
	private Set<AddressDto> addresses = new HashSet<>();
	
}
