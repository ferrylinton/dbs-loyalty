package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class TadaOrder extends AbstractAuditing implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;

    @Column(name = "order_reference", length = 20, nullable = false)
    private String orderReference;
	
	@JsonProperty("payment")
	@OneToOne(mappedBy = "tadaOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private TadaPayment tadaPayment;
	
	@JsonProperty("recipient")
	@OneToOne(mappedBy = "tadaOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private TadaRecipient tadaRecipient;
	
	@JsonProperty("items")
	@Valid
	@NotEmpty
	@OneToMany(mappedBy = "tadaOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<TadaItem> tadaItems;

	@Lob
	@Type(type = "org.hibernate.type.TextType")
	@Column(name = "response")
    private String response;

}
