package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class of Customer
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Entity
@Table(	
	name = "c_customer",
	uniqueConstraints = {
		@UniqueConstraint(name = "c_customer_email_uq", columnNames = { "email" })
	}
)
public class Customer extends AbstractUUID implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "{validation.notnull.email}")
	@Pattern(regexp = Constant.EMAIL_REGEX, message = "{validation.pattern.email}")
    @Size(min = 5, max = 50, message = "{validation.size.email}")
	@Column(name = "email", length = 50, nullable = false)
	private String email;
	
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
	@Column(nullable = false, columnDefinition = "DATE")
	private LocalDate dob;
	
	@JsonIgnore
	@Size(min=6, max = 30, message = "{validation.size.password}")
	@Transient
	private String passwordPlain;
	
	@JsonIgnore
	@Column(name = "password_hash", length = 100, nullable = false)
	private String passwordHash;

	@JsonIgnore
	@Column(name = "activated", nullable = false)
	private Boolean activated = true;

	@JsonIgnore
    @Lob
    @Column(name = "image_bytes", columnDefinition="BLOB")
    private byte[] imageBytes;

	@JsonIgnore
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<LovedOne> lovedOnes = new HashSet<>();
    
	@Transient
	private String imageUrl;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswordPlain() {
		return passwordPlain;
	}

	public void setPasswordPlain(String passwordPlain) {
		this.passwordPlain = passwordPlain;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

	public byte[] getImageBytes() {
		return imageBytes;
	}

	public void setImageBytes(byte[] imageBytes) {
		this.imageBytes = imageBytes;
	}

	public Set<LovedOne> getLovedOnes() {
		return lovedOnes;
	}

	public void setLovedOnes(Set<LovedOne> lovedOnes) {
		this.lovedOnes = lovedOnes;
	}

	public String getImageUrl() {
		return String.format("%s/api/customers/%s/image", UrlUtil.contextPath, getId());
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
}
