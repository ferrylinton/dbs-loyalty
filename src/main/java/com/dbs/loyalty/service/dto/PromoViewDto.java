package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="Promo", description = "Promo's data")
@Setter
@Getter
public class PromoViewDto extends PromoDto {

	@ApiModelProperty(value = "Promo's image url", example = "/api/promos/{id}/image", position = 5)
	private String imageUrl;
	
	@ApiModelProperty(value = "Promo's term and condition url", example = "/api/promos/{id}/term", position = 6)
	private String termUrl;
	
}
