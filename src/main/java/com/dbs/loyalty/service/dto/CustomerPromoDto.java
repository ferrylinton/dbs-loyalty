package com.dbs.loyalty.service.dto;

import com.dbs.loyalty.domain.Customer;
import com.dbs.loyalty.domain.Promo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerPromoDto extends AbstractAuditDto {
	
	private String id;

	private Customer customer;

	private Promo promo;
	
}
