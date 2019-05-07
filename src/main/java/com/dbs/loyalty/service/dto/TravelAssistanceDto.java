package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of TravelAssistance DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="TravelAssistance", description = "TravelAssistance's data")
@RequiredArgsConstructor
@Setter
@Getter
public class TravelAssistanceDto {

	@ApiModelProperty(value = "Total's customer point", example = "3000", position = 0)
	@NonNull
	private Integer total;
	
}
