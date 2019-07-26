package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Address DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="Address", description = "Address's data")
@Setter
@Getter
public class AddressDto {
	
	@NotNull
	@ApiModelProperty(value = "Label", example = "Primary | Secondary", required = true, position = 0)
	private String label;

	@NotNull
	@Size(min = 2, max = 250)
	@ApiModelProperty(value = "Address", example = "PT Infovesta Utama, Total Building fl. 10 Jl. Let. Jend S. Parman Kav. 106A", required = true, position = 1)
	private String address;
	
	@NotNull
	@Size(min = 2, max = 250)
	@ApiModelProperty(value = "City", example = "Jakarta Barat", required = true, position = 2)
	private String city;
	
	@NotNull
	@Size(min = 2, max = 20)
	@ApiModelProperty(value = "PostalCode", example = "11440", required = true, position = 3)
	private String postalCode;
	
}
