package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.converter.HashMapConverter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of TADA Order
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id", "orderReference" }, callSuper = false)
@ToString(of = { "id", "orderReference" })
@Entity
@Table(	
	name = "tada_order", 
	uniqueConstraints = {
		@UniqueConstraint(name = "tada_order_order_reference_uq", columnNames = {"order_reference"})
	}
)
public class TadaOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;
	
	@NotNull
    @Column(name = "order_reference", length = 20, nullable = false)
    private String orderReference;
	
	@Convert(converter = HashMapConverter.class)
	@Column(name = "payment", nullable = false, columnDefinition="TEXT")
    private Map<String, Object> payment;
	
	@Convert(converter = HashMapConverter.class)
	@Column(name = "recipient", nullable = false, columnDefinition="TEXT")
    private Map<String, Object> recipient;
	
	@Convert(converter = HashMapConverter.class)
	@Column(name = "items", nullable = false, columnDefinition="TEXT")
    private List<Map<String, Object>> items;
	
}
