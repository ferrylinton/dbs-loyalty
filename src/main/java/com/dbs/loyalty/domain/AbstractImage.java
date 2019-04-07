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
public class AbstractImage extends AbstractUUID implements Serializable{

	private static final long serialVersionUID = 1L;

    @Lob
    @Column(name = "bytes", nullable = false, columnDefinition="BLOB")
    private byte[] bytes;
    
    @Column(name = "content_type", length = 50, nullable = false)
    private String contentType;
    
    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;
    
}
