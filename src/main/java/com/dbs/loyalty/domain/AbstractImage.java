package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@MappedSuperclass
public class AbstractImage implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length=36)
	private String id;
	
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
