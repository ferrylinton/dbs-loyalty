package com.dbs.loyalty.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.constant.Constant;
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
	@NotNull(message = "{validation.notnull.name}")
	@Pattern(regexp = Constant.NAME_REGEX, message = "{validation.pattern.name}")
	@Size(min = 2, max = 50, message = "{validation.size.name}")
	private String name;
	
	@ApiModelProperty(value = "Customer Loved One's phone", example = "+62 8123456789", position = 2)
	@NotNull(message = "{validation.notnull.phone}")
	@Size(min = 6, max = 20, message = "{validation.size.phone}")
	private String phone;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@ApiModelProperty(value = "Customer Loved One's date of birth", example = "21-01-1980", position = 3)
	@NotNull(message = "{validation.notnull.dob}")
	private Date dob;
	
}
