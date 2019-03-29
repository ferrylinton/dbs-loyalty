package com.dbs.loyalty.service.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.enumeration.CustomerType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(description = "Customer's data")
public class CustomerDto extends AbstractAuditDto {
	
	private String id;

	@ApiModelProperty(value = "Customer's email", example = "johnsmith@dbs.com", required = true)
	@NotNull(message = "{validation.notnull.email}")
	@Pattern(regexp = Constant.EMAIL_REGEX, message = "{validation.pattern.email}")
    @Size(min = 5, max = 50, message = "{validation.size.email}")
	private String email;
	
	@ApiModelProperty(value = "Customer's name", example = "John Smith", required = true)
	@NotNull(message = "{validation.notnull.name}")
	@Pattern(regexp = Constant.NAME_REGEX, message = "{validation.pattern.name}")
	@Size(min = 2, max = 50, message = "{validation.size.name}")
	private String name;
	
	@ApiModelProperty(value = "Customer's phone", example = "+62 8123456789", required = true)
	@NotNull(message = "{validation.notnull.phone}")
	@Size(min = 6, max = 20, message = "{validation.size.phone}")
	private String phone;
	
	@ApiModelProperty(value = "Customer's type", example = "TPC", required = true)
	private CustomerType customerType;
	
	@ApiModelProperty(value = "Customer's date of birth", example = "1980-01-21", required = true)
	@NotNull(message = "{validation.notnull.dob}")
	private Date dob;
	
	@JsonIgnore
	@Size(min=6, max = 30, message = "{validation.size.password}")
	private String passwordPlain;
	
	@JsonIgnore
	private String passwordHash;

	@JsonIgnore
	private boolean activated;

	private String imageString;
	
	@JsonIgnore
	private MultipartFile file;
    
}
