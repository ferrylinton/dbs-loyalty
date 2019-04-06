package com.dbs.loyalty.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class ImageDto{

	private String id;
	
	@NonNull
    private byte[] bytes;

	@NonNull
    private String contentType;
    
	@NonNull
    private Integer width;

	@NonNull
    private Integer height;
    
}
