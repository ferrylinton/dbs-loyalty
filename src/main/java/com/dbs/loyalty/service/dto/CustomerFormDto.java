package com.dbs.loyalty.service.dto;

import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerFormDto extends CustomerDto {

	@JsonIgnore
	@Size(min=6, max = 30, message = "{validation.size.password}")
	private String passwordPlain;

	private String passwordHash;

	private boolean activated = true;
	
	private boolean locked = false;
	
	private String fileImageId;

	@JsonIgnore
    private MultipartFile imageFile;
	
}
