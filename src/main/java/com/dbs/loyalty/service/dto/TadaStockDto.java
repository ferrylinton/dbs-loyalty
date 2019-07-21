package com.dbs.loyalty.service.dto;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaStockDto {
	
	private Integer id;
	
	private String stockType;
	
	private String name;
	
	private String terms;
	
	private Integer price;
	
	private String defaultExpType;
	
	private Object defaultExp;
	
	private Instant defaultExpDate;
	
	private Boolean isLimited;
	
	private Integer quantity;
	
	private String fulfillmentBy;
	
	private List<String> availability;
	
	private String image;
	
	private Integer weight;
	
	private Integer length;
	
	private Integer height;
	
	private Integer width;
	
	private Boolean active;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	private Instant deletedAt;
	
}
