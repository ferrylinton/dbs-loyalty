package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of City
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "name" }, callSuper = false)
@Entity
@Table(	
	name = "mst_city", 
	uniqueConstraints = {
		@UniqueConstraint(name = "mst_city_name_uq", columnNames = {"name"})
	}
)
public class City implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	private Integer id;
	
	@Transient
	private Integer ProvinceId;

	@Column(name = "name", length = 50)
    private String name;
	
	@Column(name = "active")
	private Boolean active = true;
	
	@Column(name = "created_at")
	private Instant createdAt;
	
	@Column(name = "updated_at")
	private Instant updatedAt;
	
	@Column(name = "deleted_at")
	private Instant deletedAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false, foreignKey = @ForeignKey(name = "mst_city_fk"))
	private Province province;

	@Override
	public String toString() {
		return name;
	}

}
