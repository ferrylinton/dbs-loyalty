package com.dbs.loyalty.service.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbstractImageDto extends AbstractAuditDto{
	
    private byte[] imageBytes;

    private String imageContentType;

    private Integer imageWidth;

    private Integer imageHeight;
    
    @JsonIgnore
    private MultipartFile file;
    
}
