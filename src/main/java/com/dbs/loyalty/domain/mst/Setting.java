package com.dbs.loyalty.domain.mst;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.AbstractTask;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Setting
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id", "name" }, callSuper = false)
@ToString(of = { "id", "name", "value" })
@Entity
@Table(	
	name = "mst_setting", 
	uniqueConstraints = {
		@UniqueConstraint(name = "mst_setting_name_uq", columnNames = {"name" })
	}
)
public class Setting extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DomainConstant.ID, length=22)
    @GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;

	@Column(name = "name", length = 150, nullable = false)
	private String name;
	
	@Column(name = "value", length = 150, nullable = false)
	private String value;
	
}
