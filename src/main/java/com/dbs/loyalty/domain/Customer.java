package com.dbs.loyalty.domain;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.config.constant.DateConstant;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.RegexConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of Customer
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(	
	name = "cst_customer",
	uniqueConstraints = {
		@UniqueConstraint(name = "cst_customer_cif_uq", columnNames = { "cif" }),
		@UniqueConstraint(name = "cst_customer_email_uq", columnNames = { "email" })
	}
)
public class Customer extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;
	
	@Size(min = 2, max = 30)
	@Column(name = "cif", length = 30, nullable = false)
	private String cif;
	
	@Pattern(regexp = RegexConstant.EMAIL, message = RegexConstant.EMAIL_MESSAGE)
    @Size(min = 5, max = 50)
	@Column(name = "email", length = 50, nullable = false)
	private String email;
	
	@Size(min = 2, max = 50)
	@Column(name = "first_name", length = 50, nullable = false)
	private String firstName;
	
	@Size(min = 2, max = 50)
	@Column(name = "last_name", length = 50, nullable = false)
	private String lastName;
	
	@Size(min = 6, max = 20)
	@Column(name = "phone", length = 20, nullable = false)
	private String phone;
	
	@Column(name = "customer_type", length = 10, nullable = false)
	private String customerType;

	@DateTimeFormat(pattern = DateConstant.JAVA_DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateConstant.JAVA_DATE)
	@Column(name = "dob", nullable = false)
	private LocalDate dob;

	@JsonIgnore
	@Column(name = "password_hash", length = 100, nullable = true)
	private String passwordHash;
	
	@Column(name = "activated", nullable = false)
	private Boolean activated = false;
	
	@Column(name = "locked", nullable = false)
	private Boolean locked = false;

	@JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<LovedOne> lovedOnes = new HashSet<>();
	
	@OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<Address> addresses = new HashSet<>();

    @Transient
    private String image;
    
    @JsonIgnore
    @Transient
    private MultipartFile multipartFileImage;
    
}
