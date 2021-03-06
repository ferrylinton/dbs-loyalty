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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
	@Column(name = "tada_item_id", length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String tadaItemId;
	
	@NotNull
	@Column(name = "id", length=30)
	private String id;

	@NotNull
    @Column(name = "variant_id", length = 50)
    private String variantId;

	@NotNull
	@JsonProperty(value = "price", access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "price")
    private Integer price;
    
	@Column(name = "point")
	@JsonProperty(value = "point", access = JsonProperty.Access.WRITE_ONLY)
    private Integer point;
	
	@NotNull
	@Column(name = "name")
	@JsonProperty(value = "name", access = JsonProperty.Access.WRITE_ONLY)
    private String name;
	
	@Column(name = "description")
	@JsonProperty(value = "description", access = JsonProperty.Access.WRITE_ONLY)
    private String description;
	
	@Column(name = "image")
	@JsonProperty(value = "image", access = JsonProperty.Access.WRITE_ONLY)
    private String image;
    
    @NotNull
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "payment_currency", length = 50)
    private String paymentCurrency;
    
    @Column(name = "phone", length = 50)
    private String phone;
    
    @Column(name = "custom_price")
    private Integer customPrice;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tada_order_id", foreignKey = @ForeignKey(name = "trx_tada_item_fk"))
    private TadaOrder tadaOrder;
    
}
