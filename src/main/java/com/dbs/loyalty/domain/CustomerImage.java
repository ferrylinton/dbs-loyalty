package com.dbs.loyalty.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
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
	
	@OneToOne
    @JoinColumn(name = "id", foreignKey = @ForeignKey(name = "c_customer_image_fk"))
    @MapsId
    private Customer customer;
    
}
