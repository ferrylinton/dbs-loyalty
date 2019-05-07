package com.dbs.loyalty.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;

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
public class DepartureDto {

	@ApiModelProperty(value = "Service Type", example = "Meet & Assist Only", position = 0)
	@NotNull
	private String serviceType;
	
	@ApiModelProperty(value = "Airport", position = 1)
	@NotNull
	private AirportDto airport;

	@ApiModelProperty(value = "Flight Date", position = 2, example = "2019-09-20")
	@NotNull
	private LocalDate flightDate;
	
	@ApiModelProperty(value = "Flight Time", position = 3, example = "14:00")
	@NotNull
	private LocalTime flightTime;

	@ApiModelProperty(value = "Flight Code", position = 4, example = "14:00")
	@NotNull
	private String flightCode;
	
	@ApiModelProperty(value = "Flight Time", position = 5, example = "14:00")
	@NotNull
	private String flightNumber;
	
	@ApiModelProperty(value = "Customer Phone", position = 6, example = "087788527870")
	@NotNull
	private String customerPhone;
	
	@ApiModelProperty(value = "Guest Phone", position = 7, example = "087788527870")
	private String guestPhone;
	
	@ApiModelProperty(value = "Pickup from home", position = 8, example = "true")
	@NotNull
	private boolean pickupFromHome;
	
	@ApiModelProperty(value = "Number Of Passengers", position = 9, example = "2")
	@NotNull
	private int numberOfPassengers;
	
	@ApiModelProperty(value = "Number Of Luggages", position = 10, example = "5")
	@NotNull
	private int numberOfLuggages;
	
	@ApiModelProperty(value = "Pickup Address", position = 11, example = "Jl. Cendrwasih Jakarta Barat, Jakarta")
	private String pickupAddress;
	
	@ApiModelProperty(value = "Pickup Time", position = 12, example = "14:00")
	private LocalTime pickupTime;

}
