package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of TravelAssistance DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="TravelAssistance", description = "TravelAssistance's limit")
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class TravelAssistanceDto {

	@ApiModelProperty(value = "Travel Assistance's limit", example = "5", position = 0)
	@NonNull
	private Integer total;
	
}
