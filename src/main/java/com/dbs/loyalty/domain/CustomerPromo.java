package com.dbs.loyalty.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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

	@Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "c_customer_promo_fk1"))
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_id", nullable = false, foreignKey = @ForeignKey(name = "c_customer_promo_fk2"))
	private Promo promo;
	
}
