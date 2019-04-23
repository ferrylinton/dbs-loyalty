package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Role
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id", "name" }, callSuper = false)
@Entity
@Table(	
	name = "m_role", 
	uniqueConstraints = {
		@UniqueConstraint(name = "m_role_name_uq", columnNames = {"name"})
	}
)
public class Role extends AbstractAuditing implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;
	
	@Pattern(regexp = Constant.NAME_REGEX, message = "{validation.pattern.name}")
	@Size(min = 2, max = 40, message = "{validation.size.name}")
    @Column(name = "name", length = 40, nullable = false)
    private String name;
	
	@NotEmpty(message = "{validation.notempty.authorities}")
	@ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(	
    	name = "m_role_authority",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "m_role_authority_fk1")),
        inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "m_role_authority_fk2"))
    )
    private Set<Authority> authorities = new HashSet<>();

	@JsonIgnore
	@Column(name = "pending", nullable = true)
	private Boolean pending = false;
	
	@Override
	public String toString() {
		return id + "," + name;
	}
	
}
