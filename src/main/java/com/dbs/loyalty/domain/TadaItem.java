package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of TADA Item
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@ToString(of = { "id" })
@Entity
@Table(name = "trx_tada_item")
public class TadaItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DomainConstant.ID, length=50)
	private String id;

    @Column(name = "variant_id", length = 50)
    private String variantId;

    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "payment_currency", length = 50)
    private String paymentCurrency;
    
    @Column(name = "phone", length = 50)
    private String phone;
    
    @Column(name = "custom_price", precision=10, scale=0)
    private Double customPrice;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tada_order_id", foreignKey = @ForeignKey(name = "trx_tada_item_fk"))
    private TadaOrder tadaOrder;
    
}
