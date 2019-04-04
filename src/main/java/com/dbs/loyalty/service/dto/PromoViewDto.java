package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value="PromoView", description = "Promo's data")
@Setter
@Getter
public class PromoViewDto {

	@ApiModelProperty(value = "Promo's id", example = "In1IverC", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "Promo's code", example = "100", required = true, position = 1)
	@NotNull(message = "{validation.notnull.code}")
    @Size(min=2, max = 50, message = "{validation.size.code}")
	private String code;
	
	@ApiModelProperty(value = "Promo's title", example = "Nilai Tukar Kompetitif", required = true, position = 3)
	@NotNull(message = "{validation.notnull.title}")
    @Size(min=2, max = 150, message = "{validation.size.title}")
	private String title;
	
	@ApiModelProperty(value = "Promo's description", example = "Nikmati nilai tukar kompetitif Dollar Singapura terhadap Rupiah", required = true, position = 4)
	@NotNull(message = "{validation.notnull.description}")
    @Size(min=2, max = 255, message = "{validation.size.description}")
	private String description;
	
	@ApiModelProperty(value = "Promo's image url", example = "/api/promos/{id}/image", position = 5)
	private String imageUrl;
	
	@ApiModelProperty(value = "Promo's term and condition url", example = "/api/promos/{id}/term", position = 6)
	private String termAndConditionUrl;
	
}
