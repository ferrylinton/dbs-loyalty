package com.dbs.loyalty.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Bank Transaction Product DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="BankTransactionProduct", description = "BankTransactionProduct's data")
@Setter
@Getter
public class TrxProductDto {

	@ApiModelProperty(value = "BankTransactionProduct's id", example = "6nJfmxAD6GWtsehXfSkShg", required = true, position = 0)
	private String id;
	
	@ApiModelProperty(value = "BankTransactionProduct's name", example = "Airport Assistance", required = true, position = 1)
	private String name;
	
	@ApiModelProperty(value = "BankTransactionProduct's point", example = "200", required = true, position = 2)
	private Integer point;

	@ApiModelProperty(value = "BankTransactionProduct's description", example = "Description...", required = true, position = 3)
	private String description;
	
	@ApiModelProperty(value = "BankTransactionProduct's image url", example = "/api/trx-products/{id}/image", position = 4)
	private String imageUrl;

}
