package com.dbs.loyalty.service.dto;

import java.time.Instant;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Country DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Country", description = "Country's data")
@Setter
@Getter
public class CountryDto {

	private Integer id;
	
	private String code;
	
	@ApiModelProperty(value = "Country's name", example = "Indonesia", position = 0)
    private String name;
	
	private Boolean active;
	
	private Instant createdAt;
	
	private Instant updatedAt;
	
	private Instant deletedAt;
	
	@ApiModelProperty(value = "List of airports", position = 1)
	private List<AirportDto> airports;
	
}
