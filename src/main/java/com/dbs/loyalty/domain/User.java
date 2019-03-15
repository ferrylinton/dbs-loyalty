package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ColumnDefault;

import com.dbs.loyalty.config.Constant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Class of User
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Entity
@Table(	
	name = "m_user",
	uniqueConstraints = {
		@UniqueConstraint(name = "m_user_username_uq", columnNames = { "username" }),
		@UniqueConstraint(name = "m_user_email_uq", columnNames = { "email" })
	}
)
public class User extends AbstractId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "{validation.notnull.username}")
	@Pattern(regexp = Constant.USERNAME_REGEX, message = "{validation.pattern.username}")
	@Size(min = 2, max = 50, message = "{validation.size.username}")
	@Column(name = "username", length = 50, nullable = false)
	private String username;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@JsonIgnore
	@Transient
	private String passwordPlain;
	
	@Column(name = "password_hash", length = 100)
	private String passwordHash;

	@Column(name = "activated", nullable = false)
	private Boolean activated = true;
	
	@Column(name = "authenticate_from_db", nullable = false)
	private Boolean authenticateFromDb = true;
	
	@JsonIgnoreProperties("authorities")
    @ColumnDefault("NULL")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = true, foreignKey = @ForeignKey(name = "m_user_fk"))
    private Role role;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Boolean getAuthenticateFromDb() {
		return authenticateFromDb;
	}

	public void setAuthenticateFromDb(Boolean authenticateFromDb) {
		this.authenticateFromDb = authenticateFromDb;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
