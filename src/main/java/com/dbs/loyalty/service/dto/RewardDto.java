package com.dbs.loyalty.service.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Reward DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Reward", description = "Reward's data")
@Setter
@Getter
public class RewardDto{

	@ApiModelProperty(value = "Reward's point", example = "500", position = 0)
	private Integer point;
	
	@ApiModelProperty(value = "Reward's expiry date", example = "2019-08-01", position = 1)
	private LocalDate expiryDate;
	
}
