package com.dbs.loyalty.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "c_customer_image")
public class CustomerImage extends AbstractImage{

	private static final long serialVersionUID = 1L;

	@OneToOne(mappedBy = "customerImage", fetch = FetchType.LAZY)
    private Customer customer;
	
}
