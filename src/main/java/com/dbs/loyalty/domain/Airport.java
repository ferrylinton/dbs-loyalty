package com.dbs.loyalty.domain;

import static com.dbs.loyalty.config.constant.DomainConstant.ID;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR;
import static com.dbs.loyalty.config.constant.DomainConstant.ID_GENERATOR_STRATEGY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Airport
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id", "name" }, callSuper = false)
@ToString(of = { "id", "name" })
@Entity
@Table(	
	name = "a_airport", 
	uniqueConstraints = {
		@UniqueConstraint(name = "a_airport_name_uq", columnNames = {"name"})
	}
)
public class Airport extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = ID, length=22)
	@GenericGenerator(name = ID_GENERATOR, strategy = ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = ID_GENERATOR)
	private String id;

	@Size(min = 2, max = 100, message = "{validation.size.name}")
    @Column(name = "name", length = 100, nullable = false)
    private String name;
	
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", nullable = true, foreignKey = @ForeignKey(name = "a_airport_fk"))
    private Country country;

}
