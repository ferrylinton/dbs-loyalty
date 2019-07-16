package com.dbs.loyalty.domain.log;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.config.constant.DomainConstant;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(
	name = "log_token",
	indexes= {
		@Index(name = "log_token_email_idx", columnList = "email"),
		@Index(name = "log_token_created_date_idx", columnList = "created_date"),
	}
)
public class LogToken implements Serializable {

    private static final long serialVersionUID = 1L;
	
    @Id
	@Column(name = DomainConstant.ID, length=22)
	@GenericGenerator(name = DomainConstant.ID_GENERATOR, strategy = DomainConstant.ID_GENERATOR_STRATEGY)
	@GeneratedValue(generator = DomainConstant.ID_GENERATOR)
	private String id;
    
	@Column(name = "email", length = 50, nullable = false, updatable = false)
	private String email;
	
	@Column(name = "created_date", nullable = false, updatable = false)
	private Instant createdDate;

    @Column(name = "status", length = 10, nullable = false, updatable = false)
	private String status;
	
}
