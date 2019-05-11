package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(	
	name = "log_login",
	indexes= {
		@Index(name = "log_login_username_idx", columnList = "username"),
		@Index(name = "log_login_created_date_idx", columnList = "created_date"),
		@Index(name = "log_login_ip_idx", columnList = "ip")
	}
)
public class LogLogin implements Serializable {

    private static final long serialVersionUID = 1L;
	
    @Id
	@Column(name = "id", length=22)
	@GenericGenerator(name = "IdGenerator", strategy = "com.dbs.loyalty.domain.IdGenerator")
	@GeneratedValue(generator = "IdGenerator")
	private String id;
    
	@Column(name = "username", length = 50, nullable = false, updatable = false)
	private String username;
	
	@Column(name = "ip", length = 20)
	private String ip;
	
	@Column(name = "created_date", nullable = false, updatable = false)
	private Instant createdDate;
	
    @Column(name = "status", length = 10, nullable = false, updatable = false)
	private String status;
	
	@Column(name = "browser", length = 50)
	private String browser;
	
	@Column(name = "browser_ype", length = 50)
	private String browserType;
	
	@Column(name = "browser_major_version", length = 50)
	private String browserMajorVersion;
	
	@Column(name = "device_type", length = 50)
	private String deviceType;
	
	@Column(name = "platform", length = 50)
	private String platform;
	
	@Column(name = "platform_version", length = 50)
	private String platformVersion;

}
