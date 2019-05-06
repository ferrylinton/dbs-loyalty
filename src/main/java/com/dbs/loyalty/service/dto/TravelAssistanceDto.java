package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@ApiModel(value="TravelAssistance", description = "TravelAssistance's data")
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class TravelAssistanceDto {

	@NonNull
	private Integer total;
	
}
