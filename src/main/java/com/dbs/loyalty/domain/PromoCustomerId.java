package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Promo Customer Composite Id
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Embeddable
public class PromoCustomerId implements Serializable {

	private static final long serialVersionUID = 1L;

	@NonNull
	@Column(name = "customer_id", length=22)
    private String customerId;
 
	@NonNull
	@Column(name = "promo_id", length=22)
    private String promoId;
    
}
