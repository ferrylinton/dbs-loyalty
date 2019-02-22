package com.dbs.priviledge.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "m_authority",
		uniqueConstraints = {
				@UniqueConstraint(name = "m_authority_name_uq", columnNames = { "name" })
			},
		indexes= {
				@Index(name = "m_authority_idx", columnList = "name")
			})
public class Authority extends AbstractId implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
    @Size(min = 5, max = 50)
	@Column(name = "name", length = 50, nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
