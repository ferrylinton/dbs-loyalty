package com.dbs.loyalty.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "c_customer_promo")
public class CustomerPromo extends AbstractAuditing {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private CustomerPromoId id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "c_customer_promo_fk1"))
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_id", insertable = false, updatable = false, nullable = false, foreignKey = @ForeignKey(name = "c_customer_promo_fk2"))
	private Promo promo;
	
}
