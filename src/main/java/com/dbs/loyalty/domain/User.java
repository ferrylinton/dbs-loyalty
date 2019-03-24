package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
	
	@NotNull(message = "{validation.notnull.username}")
	@Pattern(regexp = Constant.USERNAME_REGEX, message = "{validation.pattern.username}")
	@Size(min = 2, max = 50, message = "{validation.size.username}")
	@Column(name = "username", length = 50, nullable = false)
	private String username;
	
	@Column(name = "password_hash", length = 100)
	private String passwordHash;

	@Column(name = "activated", nullable = false)
	private boolean activated;
	
	@Column(name = "locked", nullable = false)
	private boolean locked;
	
	@Column(name = "user_type", nullable = false, columnDefinition="TINYINT")
	private UserType userType;
	
	@Column(name = "login_attempt_count", nullable = false, columnDefinition="TINYINT")
	private int loginAttemptCount;
	
	@JsonIgnoreProperties("authorities")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = true, foreignKey = @ForeignKey(name = "m_user_fk"))
    private Role role;
	
}
