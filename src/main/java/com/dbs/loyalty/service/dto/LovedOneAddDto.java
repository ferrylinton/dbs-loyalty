package com.dbs.loyalty.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.config.constant.RegexConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="LovedOneAddData", description = "Loved One's new data")
public class LovedOneAddDto {

	@ApiModelProperty(value = "Customer Loved One's name", example = "John Smith", position = 1)
	@NotNull
	@Pattern(regexp = RegexConstant.NAME, message = RegexConstant.NAME_MESSAGE)
	@Size(min = 2, max = 50)
	private String name;
	
	@ApiModelProperty(value = "Customer Loved One's phone", example = "+62 8123456789", position = 2)
	@NotNull
	@Size(min = 6, max = 20)
	private String phone;
	
	@ApiModelProperty(value = "Customer Loved One's date of birth", example = "21-01-1980", position = 3)
	@NotNull
	@DateTimeFormat(pattern = DateConstant.JAVA_DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateConstant.JAVA_DATE)
	private Date dob;
	
}
