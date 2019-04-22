package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="Customer", description = "Customer's data")
public class CustomerViewDto extends CustomerDto{
	
	@ApiModelProperty(value = "Customer's image url", example = "/api/customers/image", position = 6)
	private String imageUrl;
    
}
