package com.dbs.loyalty.service.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaItemDataDto {

	private Integer id;
	
	@JsonProperty("CatalogCategoryId")
	private Integer catalogCategoryId;
	
	@JsonProperty("ItemId")
	private Integer itemId;
	
	private Integer position;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	@JsonProperty("CatalogCategory")
	private TadaCatalogCategoryDto catalogCategory;
	
	@JsonProperty("Item")
	private TadaItemDto item;
	
}
