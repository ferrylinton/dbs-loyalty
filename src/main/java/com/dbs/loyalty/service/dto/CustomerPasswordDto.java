package com.dbs.loyalty.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value="CustomerPasswordData", description = "Customer's password data")
public class CustomerPasswordDto {
	
	@ApiModelProperty(value = "Customer's current password", example = "pas100", required = true, position = 0)
	@NotNull(message = "{validation.notnull.oldPassword}")
	@Size(min=4, max = 100, message = "{validation.size.password}")
	private String oldPassword;
	
	@ApiModelProperty(value = "Customer's new password", example = "new100", required = true, position = 1)
	@NotNull(message = "{validation.notnull.newPassword}")
	@Size(min=4, max = 100, message = "{validation.size.password}")
	private String newPassword;
	
	@ApiModelProperty(value = "Confirmation of Customer's new password", example = "new100", required = true, position = 2)
	@NotNull(message = "{validation.notnull.confirmNewPassword}")
	@Size(min=4, max = 100, message = "{validation.size.password}")
	private String confirmNewPassword;
	
}
