package com.dbs.loyalty.service.dto;

import java.time.LocalDate;

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
@ApiModel(value="CustomerUpdateData", description = "Customer's new data")
public class CustomerUpdateDto {

	@ApiModelProperty(value = "Customer's id", example = "1jWoVxnzv4ghUJ8HCDhVTz", position = 0, hidden = true)
	private String id;
	
	@ApiModelProperty(value = "Customer's cif", example = "100001", required = true, position = 1)
	@NotNull
	@Size(min = 2, max = 30)
	private String cif;
	
	@ApiModelProperty(value = "Customer's email", example = "johnsmith@dbs.com", required = true, position = 2)
	@NotNull
	@Pattern(regexp = RegexConstant.EMAIL, message = RegexConstant.EMAIL_MESSAGE)
    @Size(min = 5, max = 50)
	private String email;
	
	@ApiModelProperty(value = "Customer's first name", example = "John", required = true, position = 3)
	@NotNull
	@Size(min = 2, max = 50)
	private String firstName;
	
	@ApiModelProperty(value = "Customer's last name", example = "Smith", required = true, position = 4)
	@NotNull
	@Size(min = 2, max = 50)
	private String lastName;
	
	@ApiModelProperty(value = "Customer's account no", example = "1000001", required = true, position = 5)
	@NotNull
	@Size(min = 2, max = 50)
	private String accountNo;
	
	@ApiModelProperty(value = "Customer's phone", example = "+62 8123456789", required = true, position = 6)
	@NotNull
	@Size(min = 6, max = 20)
	private String phone;
	
	@ApiModelProperty(value = "Customer's date of birth", example = "27-07-1983", required = true, position = 7)
	@NotNull
	@DateTimeFormat(pattern = DateConstant.JAVA_DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateConstant.JAVA_DATE)
	private LocalDate dob;
	
}
