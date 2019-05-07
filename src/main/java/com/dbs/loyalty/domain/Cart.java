package com.dbs.loyalty.domain;


import java.io.Serializable;

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

/**
 * Class of Cart
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "p_cart")
public class Cart extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;
  
    @Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;
    
    @Column(name = "category", length = 100, nullable = false)
    private String category;
    
	@Column(name = "item_id", length=22, nullable = false)
	private String itemId;

    @Column(name = "item_name", length = 150, nullable = false)
    private String itemName;
    
    @Column(name = "item_point", nullable = false)
	private Integer itemPoint;

    @Column(name = "item_quantity", nullable = false)
	private Integer itemQuantity;
    
    @Column(name = "account_name", length = 150, nullable = false)
    private String accountName;
    
    @Column(name = "account_number", length = 50, nullable = false)
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "p_cart_fk"))
	private Customer customer;
    
}
