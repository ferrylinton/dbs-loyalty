package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of PriviledgeProduct DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="PriviledgeProduct", description = "PriviledgeProduct's data")
@Setter
@Getter
public class PriviledgeProductDto {

	@ApiModelProperty(value = "PriviledgeProduct's id", example = "6nJfmxAD6GWtsehXfSkShg", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "PriviledgeProduct's name", example = "Airport Assistance", required = true, position = 1)
	private String name;
	
	@ApiModelProperty(value = "PriviledgeProduct's point", example = "200", required = true, position = 2)
	private Integer point;

	@ApiModelProperty(value = "PriviledgeProduct's description", example = "Description...", required = true, position = 3)
	private String description;
	
	@ApiModelProperty(value = "PriviledgeProduct's content", example = "Content...", required = true, position = 4)
	private String content;
	
	@ApiModelProperty(value = "PriviledgeProduct's image url", example = "/api/product-priviledges/{id}/image", position = 5)
	private String imageUrl;
	
	@ApiModelProperty(value = "PriviledgeProduct's term and condition url", example = "/api/product-priviledges/{id}/term", position = 6)
	private String termUrl;
	
}
