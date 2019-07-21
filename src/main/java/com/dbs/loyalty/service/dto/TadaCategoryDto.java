package com.dbs.loyalty.service.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaCategoryDto {

	private Integer id;
	
	private String parentId;
	
	private String name;
	
	private String label;
	
	private Integer position;
	
	private Boolean active;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	private Instant deletedAt;
	
}
