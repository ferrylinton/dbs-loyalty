package com.dbs.loyalty.domain.mst;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.dbs.loyalty.domain.ass.Airport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Country
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString(of = { "id", "name" })
@Entity
@Table(	
	name = "mst_country", 
	uniqueConstraints = {
		@UniqueConstraint(name = "mst_country_name_uq", columnNames = {"name"}),
		@UniqueConstraint(name = "mst_country_code_uq", columnNames = {"code"})
	}
)
public class Country implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "code", length = 10)
    private String code;
	
	@Column(name = "name", length = 50)
    private String name;
	
	@Column(name = "active", length = 50)
	private Boolean active = false;
	
	@Column(name = "created_at")
	private Instant createdAt;
	
	@Column(name = "updated_at")
	private Instant updatedAt;
	
	@Column(name = "deleted_at")
	private Instant deletedAt;
	
	@OrderBy(value = "name ASC")
	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private Set<Airport> airports;

}
