package com.dbs.loyalty.service.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaEgiftPropertyDto {

	private Integer id;
	
	@JsonProperty("VariantId")
	private Integer variantId;
	
	private String terms;
	
	private Integer defaultExpiry;
	
	private String eGiftSourceType;
	
	private String eGiftType;
	
	private Boolean allowMultiVoucher;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
}
