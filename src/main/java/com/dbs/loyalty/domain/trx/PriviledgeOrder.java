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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.cst.Customer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Priviledge Order
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "trx_priviledge_order")
public class PriviledgeOrder implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Id
	@Column(name = "id", length=22)
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
    @Column(name = "item_quantity", nullable = false)
	private Integer itemQuantity;
    
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "trx_priviledge_order_fk"))
	private Customer customer;
    
}
