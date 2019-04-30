package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Abstract File
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" })
@ToString(of = {"id"})
@MappedSuperclass
public abstract class AbstractFile implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length=22)
	private String id;
	
    @Lob
    @Column(name = "bytes", nullable = false, columnDefinition="BLOB")
    private byte[] bytes;
    
    @JsonIgnore
    @Column(name = "created_by", length = 50, nullable = false, updatable = false)
    private String createdBy;

    @JsonIgnore
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;
    
}
