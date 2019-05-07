package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Product Category DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@ApiModel(value="ProductCategory", description = "Product Category's data")
@Setter
@Getter
public class ProductCategoryDto {

	@ApiModelProperty(value = "Product Category's id", example = "6nJfmxAD6GWtsehXfSkShg", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "Product Category's name", example = "Priviledge", required = true, position = 1)
	private String name;

}