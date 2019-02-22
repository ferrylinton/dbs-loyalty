package com.dbs.priviledge.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.priviledge.config.Constant;


@Entity
@Table(	name = "m_user",
		uniqueConstraints = {
				@UniqueConstraint(name = "m_user_email_uq", columnNames = { "email" })
			},
		indexes= {
				@Index(name = "m_user_idx", columnList = "email")
			})
public class User extends AbstractId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "{validation.notnull.email}")
	@Pattern(regexp = Constant.EMAIL_REGEX, message = "{validation.pattern.email}")
    @Size(min = 5, max = 50, message = "{validation.size.email}")
	@Column(name = "email", length = 50, nullable = false)
	private String email;
	
	@NotNull(message = "{validation.notnull.fullname}")
	@Size(min = 3, max = 50, message = "{validation.size.fullname}")
	@Column(name = "fullname", length = 50, nullable = false)
	private String fullname;
	
	@Size(min=6, max = 30, message = "{validation.size.password}")
	@Transient
	private String passwordPlain;
	
	@Column(name = "password_hash", length = 100, nullable = false)
	private String passwordHash;

	@Column(name = "activated", nullable = false)
	private Boolean activated = true;

    @Lob
    @Column(name = "image_bytes", columnDefinition="BLOB")
    private byte[] imageBytes;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "m_user_fk"))
    private Role role;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
