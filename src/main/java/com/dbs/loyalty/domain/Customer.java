package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.dbs.loyalty.domain.enumeration.CustomerType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Customer
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = {"email"}, callSuper = true)
@Entity
@Table(	
	name = "c_customer",
	uniqueConstraints = {
		@UniqueConstraint(name = "c_customer_email_uq", columnNames = { "email" })
	}
)
public class Customer extends AbstractUUID implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "email", length = 50, nullable = false)
	private String email;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "phone", length = 20, nullable = false)
	private String phone;
	
	@Column(name = "customer_type", length = 4, nullable = false)
	private CustomerType customerType;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dob", nullable = false)
	private Date dob;

	@Column(name = "password_hash", length = 100, nullable = false)
	private String passwordHash;

	@Column(name = "activated", nullable = false)
	private boolean activated;

	@OneToOne(mappedBy = "customer", optional=false, fetch = FetchType.LAZY)
    private CustomerImage customerImage;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<LovedOne> lovedOnes = new HashSet<>();

}
