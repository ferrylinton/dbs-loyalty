package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of Total DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Total", description = "Total")
@RequiredArgsConstructor
@Setter
@Getter
public class TotalDto {

	@ApiModelProperty(value = "Total", example = "5", position = 0)
	@NonNull
	private Integer total;
	
}
