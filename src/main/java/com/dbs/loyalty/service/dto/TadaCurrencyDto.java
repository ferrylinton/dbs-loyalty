package com.dbs.loyalty.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaCurrencyDto {

	@JsonProperty("current_page")
	private String symbol;
	
	@JsonProperty("total_page")
	private String name;
	
	@JsonProperty("symbol_native")
	private String symbolNative;
	
	@JsonProperty("decimal_digits")
	private Integer decimalDigit;
	
	private Integer rounding;
	
	private String code;
	
	@JsonProperty("name_plural")
	private String namePlural;
	
	@JsonProperty("thousand_separator")
	private String thousandSeparator;
	
	@JsonProperty("decimal_separator")
	private String decimalSeparator;
	
}
