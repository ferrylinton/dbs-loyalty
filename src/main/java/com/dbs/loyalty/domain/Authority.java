package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

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
public class Authority implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length=8)
	@GenericGenerator(name = "StringIdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "StringIdGenerator")
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("%s,%s", id, name);
	}
	
}
