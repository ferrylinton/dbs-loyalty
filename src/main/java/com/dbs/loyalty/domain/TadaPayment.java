package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of TADA Payment
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id"}, callSuper = false)
@ToString(of = { "id" })
@Entity
@Table(name = "trx_tada_payment")
public class TadaPayment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;

    @Column(name = "payment_type", length = 50)
    private String paymentType;

    @Column(name = "payment_channel", length = 50)
    private String paymentChannel;
    
    @Column(name = "payment_currency", length = 50)
    private String paymentCurrency;
    
    @Column(name = "payment_wallet_type", length = 50)
    private String paymentWalletType;
    
    @Column(name = "payment_wallet_name", length = 50)
    private String paymentWalletName;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tada_order_id", foreignKey = @ForeignKey(name = "trx_tada_payment_fk"))
    private TadaOrder tadaOrder;
    
}
