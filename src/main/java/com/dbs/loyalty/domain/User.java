package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.domain.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(	
	name = "m_user",
	uniqueConstraints = {
		@UniqueConstraint(name = "m_user_username_uq", columnNames = { "username" })
	}
)
public class User extends AbstractAuditing implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;
	
	@NotNull(message = "{validation.notnull.username}")
	@Pattern(regexp = Constant.USERNAME_REGEX, message = "{validation.pattern.username}")
	@Column(name = "username", length = 50, nullable = false)
	private String username;
	
	@Column(name = "password_hash", length = 100)
	private String passwordHash;
	
	@Transient
	@JsonIgnore
	private String passwordPlain;

	@Column(name = "activated", nullable = false)
	private Boolean activated;
	
	@Column(name = "locked", nullable = false)
	private Boolean locked;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "user_type", nullable = false, columnDefinition="TINYINT")
	private UserType userType;
	
	@JsonIgnore
	@Column(name = "login_attempt_count", nullable = false, columnDefinition="TINYINT")
	private int loginAttemptCount;
	
	@JsonIgnoreProperties("authorities")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = true, foreignKey = @ForeignKey(name = "m_user_fk"))
    private Role role;
	
}
