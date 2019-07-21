package com.dbs.loyalty.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaMerchantDto {

	private Integer id;
	
	private Integer mId;
	
	@JsonProperty("CountryName")
	private String countryName;
	
	@JsonProperty("Currency")
	private TadaCurrencyDto currency;
	
	private String brand;
	
}
