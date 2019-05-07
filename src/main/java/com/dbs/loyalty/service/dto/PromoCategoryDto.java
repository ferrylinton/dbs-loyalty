package com.dbs.loyalty.service.dto;

import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of Promo Category DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="PromoCategory", description = "Promo Category's data")
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class PromoCategoryDto {

	@ApiModelProperty(value = "Promo Category's id", example = "6nJfmxAD6GWtsehXfSkShg", required = true, position = 0)
	@NonNull
	private String id;
	
	@ApiModelProperty(value = "Promo Category's name", example = "Debit Card Promo", required = true, position = 0)
	@NonNull
	@Size(min = 2, max = 100, message = "{validation.size.name}")
	private String name;
	
	@Override
	public String toString() {
		return id + "," + name;
	}
	
}