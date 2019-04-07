package com.dbs.loyalty.service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AbstractImageDto{

    private byte[] bytes;

    private String contentType;

    private Integer width;

    private Integer height;
    
}
