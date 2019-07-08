package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Flight DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Flight", description = "Flight's data")
@Setter
@Getter
public abstract class AbstractFlightDto{

	@ApiModelProperty(value = "Airport", position = 0)
	@NotNull
	private AirportDto airport;
	
	@ApiModelProperty(value = "Type Of Service", example = "Airport Assistance", position = 1)
	@NotNull
	private String typeOfService;
	
	@ApiModelProperty(value = "Flight Date", position = 2, example = "2019-07-01T10:00:00.000Z")
	@NotNull
	private String flightDate;
	
	@ApiModelProperty(value = "Flight Code", position = 3, example = "AAA")
	@NotNull
	private String flightCode;
	
	@ApiModelProperty(value = "Flight Time", position = 4, example = "14:00")
	@NotNull
	private String flightNumber;
	
	@ApiModelProperty(value = "Customer Phone", position = 5, example = "087788527870")
	@NotNull
	private String customerPhone;
	
	@ApiModelProperty(value = "Guest Phone", position = 7, example = "087788527870")
	private String guestPhone;
	
	@ApiModelProperty(value = "Airport Transfer", position = 8, example = "true")
	@NotNull
	private boolean airportTransfer;
	
	@ApiModelProperty(value = "Number Of Passengers", position = 9, example = "2")
	@NotNull
	private int numberOfPassengers;
	
	@ApiModelProperty(value = "Number Of Luggages", position = 10, example = "2")
	@NotNull
	private int numberOfLuggages;
	
	@ApiModelProperty(value = "Pickup Time", position = 12, example = "2019-07-01T10:00:00.000Z")
	private String pickupTime;
	
	@JsonIgnore
	private String message;
}
