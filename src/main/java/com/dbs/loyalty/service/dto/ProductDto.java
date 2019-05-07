package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Product DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Product", description = "Product's data")
@Setter
@Getter
public class ProductDto {

	@ApiModelProperty(value = "Product's id", example = "646e8a2a-4ca4-459a-9da8-2a31daaecd38", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "Product's name", example = "Airport Assistance", required = true, position = 1)
	private String name;
	
	@ApiModelProperty(value = "Product's point", example = "200", required = true, position = 2)
	private Integer point;

	@ApiModelProperty(value = "Product's description", example = "Description...", required = true, position = 3)
	private String description;
	
	@ApiModelProperty(value = "Product's content", example = "Content...", required = true, position = 4)
	private String content;
	
	@ApiModelProperty(value = "Product's image url", example = "/api/products/{id}/image", position = 5)
	private String imageUrl;
	
	@ApiModelProperty(value = "Product's term and condition url", example = "/api/products/{id}/term", position = 6)
	private String termUrl;
	
}
