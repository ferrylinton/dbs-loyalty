package com.dbs.loyalty.service.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.constant.RegexConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@ApiModelProperty(value = "Customer's email", example = "ferrylinton@gmail.com", position = 0)
	@NotNull
	@NonNull
	@Pattern(regexp = RegexConstant.EMAIL, message = RegexConstant.EMAIL_MESSAGE)
    @Size(min = 5, max = 50)
	private String email;
	
	@JsonIgnore
	@ApiModelProperty(value = "Token", example = "112233", position = 1)
	@NotNull
	@NonNull
	@Min(value=100000)
	@Max(value=999999)
	private Integer token;
	
}
