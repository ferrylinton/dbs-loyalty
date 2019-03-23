package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Role
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "name" }, callSuper = true)
@ToString
@Entity
@Table(	
	name = "m_role", 
	uniqueConstraints = {
		@UniqueConstraint(name = "m_role_name_uq", columnNames = {"name"})
	}
)
public class Role extends AbstractId implements Serializable {

	private static final long serialVersionUID = 1L;

    @Column(name = "name", length = 40, nullable = false)
    private String name;
	
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(	
    	name = "m_role_authority",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "m_role_authority_fk1")),
        inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "m_role_authority_fk2"))
    )
    private Set<Authority> authorities = new HashSet<>();

}
