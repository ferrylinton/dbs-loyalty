package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Arrival DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Arrival", description = "Arrival's data")
@Setter
@Getter
public class ArrivalDto extends AbstractFlightDto {

	@ApiModelProperty(value = "Drop Off Address", position = 11, example = "Jl. Cendrwasih Jakarta Barat, Jakarta")
	private String dropOffAddress;

}
