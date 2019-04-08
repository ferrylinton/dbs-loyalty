package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public class AbstractImage extends AbstractAuditing implements Serializable{

	private static final long serialVersionUID = 1L;

    @Lob
    @Column(name = "image_bytes", nullable = false, columnDefinition="BLOB")
    private byte[] imageBytes;
    
    @Column(name = "image_content_type", length = 50, nullable = false)
    private String imageContentType;
    
    @Column(name = "image_width", nullable = false)
    private Integer imageWidth;

    @Column(name = "image_height", nullable = false)
    private Integer imageHeight;
    
}
