package com.dbs.loyalty.service.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PromoFormDto extends PromoDto {

	private MultipartFile file;
	
}
