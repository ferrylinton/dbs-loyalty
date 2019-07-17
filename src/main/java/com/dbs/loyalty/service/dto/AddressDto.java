package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Address DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Address", description = "Address's data")
@Setter
@Getter
public class AddressDto {

	private String address;
	
	private String city;
	
	private String postalCode;
	
	private Boolean primary = false;
	
}
