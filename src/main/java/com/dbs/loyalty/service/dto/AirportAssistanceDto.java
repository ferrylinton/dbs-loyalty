package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of AirportAssistance DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="AirportAssistance", description = "Airport Assistance's eligibility")
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class AirportAssistanceDto {

	@ApiModelProperty(value = "Airport Assistance's eligibility", example = "5", position = 0)
	@NonNull
	private Integer total;
	
}
