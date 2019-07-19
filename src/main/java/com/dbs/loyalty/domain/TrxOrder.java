package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Bank Transaction Order
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "trx_order")
public class TrxOrder implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;

    @NotNull
	@Column(name = "item_id", length=22, nullable = false)
	private String itemId;

    @NotNull
    @Column(name = "item_name", length = 150, nullable = false)
    private String itemName;
    
    @NotNull
    @Column(name = "item_point", nullable = false)
	private Integer itemPoint;

    @NotNull
    @Column(name = "withdraw", nullable = false)
	private Integer withdraw;
    
    @NotNull
    @Column(name = "customer_name", length = 100, nullable = false)
    private String customerName;
    
    @NotNull
    @Column(name = "account_no", length = 20, nullable = false)
    private String accountNo;
    
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "trx_priviledge_order_fk"))
	private Customer customer;
    
}
