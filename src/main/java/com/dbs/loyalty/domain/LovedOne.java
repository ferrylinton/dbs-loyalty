package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of LovedOne
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "c_loved_one")
public class LovedOne extends AbstractAuditing implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;
	
	@NotNull(message = "{validation.notnull.name}")
	@Size(min = 2, max = 50, message = "{validation.size.name}")
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@NotNull(message = "{validation.notnull.phone}")
	@Size(min = 6, max = 20, message = "{validation.size.phone}")
	@Column(name = "phone", length = 20, nullable = false)
	private String phone;
	
	@NotNull(message = "{validation.notnull.dob}")
	@Temporal(TemporalType.DATE)
	@Column(name = "dob", nullable = false, columnDefinition = "DATE")
	private Date dob;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "m_customer_fk"))
	private Customer customer;

}
