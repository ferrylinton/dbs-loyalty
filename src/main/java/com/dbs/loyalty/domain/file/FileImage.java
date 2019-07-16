package com.dbs.loyalty.domain.file;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Class of Image
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@Entity
@Table(name = "file_image")
public class FileImage extends AbstractFileImage implements Serializable {

	private static final long serialVersionUID = 1L;
	
    @JsonIgnore
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @JsonIgnore
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
    
}
