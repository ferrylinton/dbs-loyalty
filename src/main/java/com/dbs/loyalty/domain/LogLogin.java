package com.dbs.loyalty.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dbs.loyalty.domain.enumeration.LoginStatus;



@Entity
@Table(	
	name = "log_login",
	indexes= {
		@Index(name = "log_login_username_idx", columnList = "username"),
		@Index(name = "log_login_ip_idx", columnList = "ip")
	}
)
public class LogLogin implements Serializable {

    private static final long serialVersionUID = 1L;
	
    @Id
	@Column(name = "id", length=36)
	@GenericGenerator(name = "UUIDGenerator", strategy = "com.dbs.loyalty.domain.UUIDGenerator")
	@GeneratedValue(generator = "UUIDGenerator")
	private String id;
    
	@Column(name = "username", length = 50, nullable = false, updatable = false)
	private String username;
	
	@Column(name = "ip", length = 20)
	private String ip;
	
	@Column(name = "created_date", nullable = false, updatable = false)
	private Instant createdDate;
	
	@Enumerated(EnumType.ORDINAL)
    @Column(name = "login_status", length = 1, nullable = false, updatable = false)
	private LoginStatus loginStatus;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getBrowserType() {
		return browserType;
	}

	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}

	public String getBrowserMajorVersion() {
		return browserMajorVersion;
	}

	public void setBrowserMajorVersion(String browserMajorVersion) {
		this.browserMajorVersion = browserMajorVersion;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlatformVersion() {
		return platformVersion;
	}

	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}

}
