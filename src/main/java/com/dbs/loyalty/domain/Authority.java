package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Class of Authority
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Entity
@Table(	
	name = "m_authority", 
	uniqueConstraints = {
		@UniqueConstraint(name = "m_authority_name_uq", columnNames = {"name" })
	}
)
public class Authority extends AbstractId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "name", length = 50, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
