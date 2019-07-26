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

	@NotNull
	@Size(min = 22, max = 22)
	@ApiModelProperty(value = "Airport's id", example = "53EHfQ52dsQ8h9R7ezwUbZ", required = true, position = 0)
	private String id;

	@ApiModelProperty(value = "Airport's name", example = "CKG-Soekarno-Hatta", required = true, position = 1)
	@NotNull
	@Size(min = 2, max = 100)
    private String name;

	@Override
	public int compareTo(AirportDto obj) {
		return (this.getName().compareTo(obj.getName()));
	}
}
