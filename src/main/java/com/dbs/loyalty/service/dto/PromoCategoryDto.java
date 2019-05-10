package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Promo Category DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="PromoCategory", description = "Promo Category's data")
@Setter
@Getter
public class PromoCategoryDto {

	@ApiModelProperty(value = "Promo Category's id", example = "6nJfmxAD6GWtsehXfSkShg", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "Promo Category's name", example = "Debit Card Promo", required = true, position = 1)
	private String name;

}