package com.dbs.loyalty.service.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EventFormDto extends EventDto{

	private String timePeriod;
	
	private String imageFileId;
	
	private String materialFilePdfId;
	
	@JsonIgnore
    private MultipartFile imageFile;
	
	@JsonIgnore
    private MultipartFile materialFile;
	
}
