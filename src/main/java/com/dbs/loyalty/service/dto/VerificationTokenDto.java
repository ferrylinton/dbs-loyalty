package com.dbs.loyalty.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of VerificationToken DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="VerificationToken", description = "VerificationToken")
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class VerificationTokenDto {

	@ApiModelProperty(value = "Token", example = "112233", position = 0)
	@NotNull
	@Min(value=100000)
	@Max(value=999999)
	@NonNull
	private Integer token;
	
}
