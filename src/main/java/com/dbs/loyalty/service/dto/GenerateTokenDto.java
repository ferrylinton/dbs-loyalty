package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.constant.RegexConstant;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Class of GenerateToken DTO
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@ApiModel(value="GenerateTokenInput", description = "GenerateTokenInput")
@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class GenerateTokenDto {

	@ApiModelProperty(value = "Customer's email", example = "ferrylinton@gmail.com", position = 0)
	@NonNull
	@NotNull
	@Pattern(regexp = RegexConstant.EMAIL, message = RegexConstant.EMAIL_MESSAGE)
    @Size(min = 5, max = 50)
	private String email;

}
