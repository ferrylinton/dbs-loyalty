package com.dbs.loyalty.service.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractAuditDto {

	@JsonIgnore
	private String createdBy;
	
	@JsonIgnore
	private Instant createdDate;
	
	@JsonIgnore
	private String lastModifiedBy;
	
	@JsonIgnore
	private Instant lastModifiedDate;
	
}
