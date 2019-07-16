package com.dbs.loyalty.domain.file;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * Class of Abstract Image
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@MappedSuperclass
public abstract class AbstractFileImage extends AbstractFile implements Serializable{

	private static final long serialVersionUID = 1L;
    
    @Column(name = "content_type", length = 50, nullable = false)
    private String contentType;
    
    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;
    
}
