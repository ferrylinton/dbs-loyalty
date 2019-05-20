package com.dbs.loyalty.service.dto;

import java.time.Instant;

import javax.validation.constraints.NotNull;

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
public class ArrivalDto {

	@ApiModelProperty(value = "Service Type", example = "Meet & Assist Only", position = 0)
	@NotNull
	private String serviceType;
	
	@ApiModelProperty(value = "Airport", position = 1)
	@NotNull
	private AirportDto airport;

	@ApiModelProperty(value = "Flight Date", position = 2, example = "2019-07-01T10:00:00.000Z")
	@NotNull
	private Instant flightDate;

	@ApiModelProperty(value = "Flight Code", position = 4, example = "AAA")
	@NotNull
	private String flightCode;
	
	@ApiModelProperty(value = "Flight Number", position = 5, example = "10")
	@NotNull
	private String flightNumber;
	
	@ApiModelProperty(value = "Customer Phone", position = 6, example = "087788527870")
	@NotNull
	private String customerPhone;
	
	@ApiModelProperty(value = "Guest Phone", position = 7, example = "087788527870")
	private String guestPhone;
	
	@ApiModelProperty(value = "Aiport Transfer", position = 8, example = "true")
	@NotNull
	private boolean airportTransfer;
	
	@ApiModelProperty(value = "Number Of Passengers", position = 9, example = "2")
	@NotNull
	private int numberOfPassengers;
	
	@ApiModelProperty(value = "Number Of Luggages", position = 10, example = "5")
	@NotNull
	private int numberOfLuggages;
	
	@ApiModelProperty(value = "Pickup Address", position = 11, example = "Jl. Cendrwasih Jakarta Barat, Jakarta")
	private String pickupAddress;
	
	@ApiModelProperty(value = "Pickup Time", position = 12, example = "2019-07-01T10:00:00.000Z")
	private Instant pickupTime;

}
