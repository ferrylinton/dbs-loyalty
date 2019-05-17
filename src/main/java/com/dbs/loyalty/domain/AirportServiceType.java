package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Airport Service
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id", "name" }, callSuper = false)
@Entity
@Table(	
	name = "a_service_type", 
	uniqueConstraints = {
		@UniqueConstraint(name = "a_service_type_name_uq", columnNames = {"name"})
	}
)
public class AirportServiceType extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;
	
	@Size(min = 2, max = 140, message = "{validation.size.name}")
    @Column(name = "name", length = 140, nullable = false)
    private String name;
	
    @Override
	public String toString() {
		return id + "," + name;
	}
    
}
