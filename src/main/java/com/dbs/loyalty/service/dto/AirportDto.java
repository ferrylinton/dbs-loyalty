package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Airport DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Airport", description = "Airport's data")
@Setter
@Getter
public class AirportDto implements Comparable<AirportDto>{

	@NotNull(message = "{validation.notnull.id}")
	@Size(min = 22, max = 22, message = "{validation.size.id}")
	@ApiModelProperty(value = "Airport's id", example = "53EHfQ52dsQ8h9R7ezwUbZ", required = true, position = 0)
	private String id;

	@ApiModelProperty(value = "Airport's name", example = "53EHfQ52dsQ8h9R7ezwUbZ", required = true, position = 1)
	@NotNull(message = "{validation.notnull.name}")
	@Size(min = 2, max = 100, message = "{validation.size.name}")
    private String name;

	@Override
	public int compareTo(AirportDto obj) {
		return (this.getName().compareTo(obj.getName()));
	}
}
