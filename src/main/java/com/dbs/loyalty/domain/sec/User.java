package com.dbs.loyalty.domain.sec;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.RegexConstant;
import com.dbs.loyalty.domain.AbstractTask;
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
@EqualsAndHashCode(of = {"id", "username"}, callSuper = false)
@ToString(of = {"id", "username"})
@Entity
@Table(	
	name = "sec_user",
	uniqueConstraints = {
		@UniqueConstraint(name = "sec_user_username_uq", columnNames = { "username" })
	}
)
public class User extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name=DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;
	
	@Size(min = 5, max = 50)
	@Pattern(regexp = RegexConstant.USERNAME, message = RegexConstant.USERNAME_MESSAGE)
	@Column(name = "username", length = 50, nullable = false)
	private String username;
	
	@Column(name = "password_hash", length = 100)
	private String passwordHash;
	
	@Transient
	@JsonIgnore
	private String passwordPlain;

	@Column(name = "activated", nullable = false)
	private Boolean activated = true;
	
	@Column(name = "locked", nullable = false)
	private Boolean locked = false;

	@Column(name = "user_type", nullable = false, length = 10)
	private String userType;
	
	@JsonIgnore
	@Column(name = "login_attempt_count", nullable = false)
	private int loginAttemptCount;
	
	@JsonIgnoreProperties("authorities")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = true, foreignKey = @ForeignKey(name = "sec_user_fk"))
    private Role role;
	
}
