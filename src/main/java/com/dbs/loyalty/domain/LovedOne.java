package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.Constant;

/**
 * Class of LovedOne
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Entity
@Table(name = "c_loved_one")
public class LovedOne extends AbstractUUID implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull(message = "{validation.notnull.name}")
	@Pattern(regexp = Constant.NAME_REGEX, message = "{validation.pattern.name}")
	@Size(min = 2, max = 50, message = "{validation.size.name}")
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@NotNull(message = "{validation.notnull.phone}")
	@Size(min = 6, max = 20, message = "{validation.size.phone}")
	@Column(name = "phone", length = 20, nullable = false)
	private String phone;
	
	@NotNull(message = "{validation.notnull.dob}")
	@Column(name = "dob", nullable = false, columnDefinition = "DATE")
	private LocalDate dob;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "m_customer_fk"))
	private Customer customer;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}
