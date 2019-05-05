package com.dbs.loyalty.service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Departure Dto
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@ApiModel(value="Departure", description = "Departure's data")
public class DepartureDto {

	@ApiModelProperty(value = "Service Type", example = "Meet & Assist Only", position = 1)
	@NotNull(message = "{validation.notnull.serviceType}")
	@Size(min = 2, max = 150, message = "{validation.size.serviceType}")
	private String serviceType;
	
	private String airportId;

	private LocalDate flightDate;
	
	private LocalTime flightTime;

	private String flightCode;
	
	private String flightNumber;
	
	private String customerPhone;
	
	private String guestPhone;
	
	private boolean pickupFromHome;
	
	private int numberOfPassengers;
	
	private int numberOfLuggages;
	
	private String pickupAddress;
	
	private LocalTime pickupTime;

}
