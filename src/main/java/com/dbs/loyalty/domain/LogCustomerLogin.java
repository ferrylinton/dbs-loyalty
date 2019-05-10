package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.domain.enumeration.LoginStatus;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "log_customer_login")
public class LogCustomerLogin implements Serializable {

    private static final long serialVersionUID = 1L;
	
    @Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;
    
	@Column(name = "email", length = 50, nullable = false, updatable = false)
	private String email;
	
	@Column(name = "created_date", nullable = false, updatable = false)
	private Instant createdDate;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "login_status", length = 10, nullable = false, updatable = false)
	private LoginStatus loginStatus;
	
}
