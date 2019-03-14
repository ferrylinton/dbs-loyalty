package com.dbs.loyalty.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.config.CustomLdapProperties;
import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.Role;

@Service
public class LdapService {

	private final String OBJECT_CLASS = "objectclass";
	
	private final String PERSON = "person";

	private LdapTemplate ldapTemplate;
    
    private CustomLdapProperties customLdapProperties;
    
    private RoleService roleService;

	public LdapService(LdapTemplate ldapTemplate, CustomLdapProperties customLdapProperties, RoleService roleService) {
		this.ldapTemplate = ldapTemplate;
		this.customLdapProperties = customLdapProperties;
		this.roleService = roleService;
	}
    
    public Authentication authenticate(String username, String password, Role role) {
    	AndFilter filter = new AndFilter()
        	.and(new EqualsFilter(OBJECT_CLASS, PERSON))
        	.and(new EqualsFilter(customLdapProperties.getAttribute(), username));
    	
    	boolean authenticated = ldapTemplate.authenticate(customLdapProperties.getLdapProperties().getBase(), filter.toString(), password);
    
    	if(authenticated) {
    		return new UsernamePasswordAuthenticationToken(username, password, getAuthorities(role));
    	}else {
    		throw new BadCredentialsException(Constant.EMPTY);
    	}
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(Role role) {
    	if(role == null) {
    		Optional<Role> current = roleService.findWithAuthoritiesByName(customLdapProperties.getRole());

    		if(current.isPresent()) {
    			role = current.get();
    		}
    	}
    	
    	Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		
    	if(role != null) {
    		for(Authority authority : role.getAuthorities()){
    			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
    		}
    	}
    	
        return grantedAuthorities;
    }
}
