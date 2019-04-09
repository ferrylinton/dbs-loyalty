package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="PromoCarousel", description = "Promo Carousel's data")
@Setter
@Getter
public class CarouselDto {

	@ApiModelProperty(value = "Promo's id", example = "In1IverC", required = true, position = 0)
	private String id;

	@ApiModelProperty(value = "Promo's title", example = "Nilai Tukar Kompetitif", required = true, position = 3)
	private String title;

	@ApiModelProperty(value = "Promo's image url", example = "/api/promos/{id}/image", position = 5)
	private String imageUrl;
	
}
