package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.dbs.loyalty.domain.enumeration.UserType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Class of User
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@EqualsAndHashCode(of = { "username" }, callSuper = true)
@ToString
@Entity
@Table(	
	name = "m_user",
	uniqueConstraints = {
		@UniqueConstraint(name = "m_user_username_uq", columnNames = { "username" })
	}
)
public class User extends AbstractId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "username", length = 50, nullable = false)
	private String username;
	
	@Column(name = "password_hash", length = 100)
	private String passwordHash;

	@Column(name = "activated", nullable = false)
	private boolean activated;
	
	@Column(name = "locked", nullable = false)
	private boolean locked;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "user_type", nullable = false, columnDefinition="TINYINT")
	private UserType userType;
	
	@Column(name = "login_attempt_count", nullable = false, columnDefinition="TINYINT")
	private int loginAttemptCount;
	
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = true, foreignKey = @ForeignKey(name = "m_user_fk"))
    private Role role;
	
}
