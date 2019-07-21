package com.dbs.loyalty.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaItemDto {

	private Integer id;
	
	private Integer mId;
	
	@JsonProperty("CategoryId")
	private Integer CategoryId;
	
	@JsonProperty("VendorId")
	private Integer VendorId;
	
	private Boolean isDigital;
	
	private String itemType;
	
	private String swapRedeem;
	
	private String name;
	
	private String description;
	
	private String image;
	
	private String prefix;
	
	private String deliveryType;
	
	private Boolean isLimited;
	
	private Integer limitQty;
	
	private Integer gcfsItemId;
	
	private Boolean active;
	
	private TadaShippingFeeRuleDto shippingFeeRule;
	
	@JsonProperty("Category")
	private TadaCategoryDto category;
	
	@JsonProperty("Variants")
	private List<TadaVariantDto> variants;
	
	@JsonProperty("ambassadorAttribute")
	private List<String> ambassadorAttributes;
	
	@JsonProperty("Merchant")
	private TadaMerchantDto merchant;
	
}
