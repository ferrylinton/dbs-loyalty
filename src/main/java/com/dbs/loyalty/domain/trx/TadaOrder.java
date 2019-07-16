package com.dbs.loyalty.domain.trx;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.cst.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
	name = "trx_tada_order", 
	uniqueConstraints = {
		@UniqueConstraint(name = "trx_tada_order_reference_uq", columnNames = {"order_reference"})
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

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "content", nullable = false)
    private String content;
	
	@Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "trx_tada_order_fk"))
	private Customer customer;
    
}
