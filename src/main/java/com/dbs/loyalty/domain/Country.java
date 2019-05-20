package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;

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
@EqualsAndHashCode(of = { "id", "name" }, callSuper = false)
@ToString(of = { "id", "name" })
@Entity
@Table(	
	name = "a_country", 
	uniqueConstraints = {
		@UniqueConstraint(name = "a_country_name_uq", columnNames = {"name"})
	}
)
public class Country extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;
	
	@Column(name = "name", length = 40, nullable = false)
    private String name;
	
	@OrderBy(value = "name ASC")
	@OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private Set<Airport> airports;

}
