package com.dbs.loyalty.domain.prm;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.dbs.loyalty.util.SecurityUtil;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Promo Customer Composite Id
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Embeddable
public class PromoCustomerId implements Serializable {

	private static final long serialVersionUID = 1L;

	public PromoCustomerId(String promoId) {
		this.promoId = promoId;
		this.customerId = SecurityUtil.getId();
	}

	@Column(name = "customer_id", length=22)
    private String customerId;

	@Column(name = "promo_id", length=22)
    private String promoId;
    
}
