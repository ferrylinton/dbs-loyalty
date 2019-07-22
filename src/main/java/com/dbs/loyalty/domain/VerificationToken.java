package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class of Verification Token
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "id" }, callSuper = false)
@Entity
@Table(name = "sec_verification_token")
public class VerificationToken implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name=DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;
	
	@Column(name = "email", length = 50, nullable = false)
	private String email;
	
	@Column(name = "token", nullable = false)
	private Integer token;
	
	@Column(name = "expiry_date", nullable = false, updatable = false)
    private Instant expiryDate;
	
	@JsonIgnore
	@Column(name = "verified", nullable = true)
	private Boolean verified;
	
	@Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

	@Column(name = "last_modified_date")
    private Instant lastModifiedDate;
	
	@Transient
	@JsonIgnore
	private Customer customer;
	
}
