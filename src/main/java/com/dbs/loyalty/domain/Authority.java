package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Authority
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id", "name" })
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

	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
}
