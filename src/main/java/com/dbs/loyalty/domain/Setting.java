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
 * Class of Setting
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id", "name" }, callSuper = false)
@Entity
@Table(	
	name = "s_setting", 
	uniqueConstraints = {
		@UniqueConstraint(name = "s_setting_name_uq", columnNames = {"name" })
	}
)
public class Setting extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;

	@Column(name = "name", length = 150, nullable = false)
	private String name;
	
	@Column(name = "value", length = 150, nullable = false)
	private String value;
	
}
