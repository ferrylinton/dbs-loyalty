package com.dbs.loyalty.service.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TadaPageDto {

	@JsonProperty("current_page")
	private Integer currentPage;
	
	@JsonProperty("total_page")
	private Integer totalPage;
	
	private Integer count;
	
	private List<TadaItemDataDto> data;
	
}
