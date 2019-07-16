package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Authority
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" })
@ToString(of = { "id", "name" })
@Entity
@Table(	
	name = "sec_authority", 
	uniqueConstraints = {
		@UniqueConstraint(name = "sec_authority_name_uq", columnNames = {"name" })
	}
)
public class Authority implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name=DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;

	@Column(name = "name", length = 50, nullable = false)
	private String name;

}
