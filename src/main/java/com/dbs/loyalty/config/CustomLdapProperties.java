package com.dbs.loyalty.config;

import org.springframework.boot.autoconfigure.ldap.LdapProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ldap")
public class CustomLdapProperties{

	private String domain;
	
	private String attribute = "sAMAccountName";
	
	private String role = "Maker";
	
	private LdapProperties ldapProperties;
	
	public CustomLdapProperties(LdapProperties ldapProperties) {
		this.ldapProperties = ldapProperties;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LdapProperties getLdapProperties() {
		return ldapProperties;
	}

}
