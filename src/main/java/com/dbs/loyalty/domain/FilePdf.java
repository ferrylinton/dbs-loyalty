package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * Class of File Pdf
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@Entity
@Table(name = "f_pdf")
public class FilePdf extends AbstractFile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @JsonIgnore
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
	
}
