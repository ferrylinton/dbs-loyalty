package com.dbs.loyalty.service.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaCatalogCategoryDto {

	private Integer id;
	
	private Integer CatalogId;
	
	private Integer CategoryId;
	
	private Integer position;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	private Instant deletedAt;
	
}
