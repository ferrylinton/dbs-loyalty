package com.dbs.loyalty.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ldap")
public class LdapProperties{

	private String domain;
	
	private String url;
	
	private String principal;
	
	private String credentials;
	
	private String base;
	
	private String searchFilter = "(&(sAMAccountName=%s@%s))";

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getSearchFilter() {
		return searchFilter;
	}
	
	public String getSearchFilter(String username) {
		if(domain == null) {
			return String.format(searchFilter, username);
		}else {
			return String.format(searchFilter, username, domain);
		}
	}

	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}
	
}
