package com.dbs.loyalty.service.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaVariantDto {

	private Integer id;
	
	private String sku;
	
	private Integer ItemId;
	
	@JsonProperty("VariantRefId")
	private String variantRefId;
	
	@JsonProperty("StockId")
	private String stockId;
	
	private String name;
	
	private String description;
	
	private String image;
	
	private String valueType;
	
	private Integer minPrice;
	
	private Boolean isMultiPrice;
	
	private Integer price;
	
	@JsonProperty("VendorCodeId")
	private String vendorCodeId;
	
	private String sourceType;
	
	private String sourceId;
	
	private Boolean active;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	private Instant deletedAt;
	
	@JsonProperty("Stock")
	private TadaStockDto tadaStock;

	@JsonProperty("EgiftProperty")
	private TadaEgiftPropertyDto egiftProperty;
	
	@JsonProperty("VendorCode")
	private String vendorCode;
	
	@JsonProperty("VariantPrices")
	private List<String> variantPrices;
	
}
