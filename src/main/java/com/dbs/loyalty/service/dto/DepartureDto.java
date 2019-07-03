package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Departure DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Departure", description = "Departure's data")
@Setter
@Getter
public class DepartureDto extends AbstractFlightDto {

	@ApiModelProperty(value = "Guest Name", position = 6, example = "Ferry Linton Hutapea")
	private String guestName;

	@ApiModelProperty(value = "Pickup Address", position = 11, example = "Jl. Cendrwasih Jakarta Barat, Jakarta")
	private String pickupAddress;

}
