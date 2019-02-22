package com.dbs.priviledge.thymeleaf.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dbs.priviledge.domain.Authority;
import com.dbs.priviledge.domain.User;
import com.dbs.priviledge.service.AuthorityService;

@Service
public class SecurityService {

	private Authority rolePost;
	
	private final AuthorityService authorityService;
	
	public SecurityService(AuthorityService authorityService) {
		this.authorityService = authorityService;
	}
	
	public boolean hasRolePost(User user) {
		if(user.getRole() == null) {
			return false;
		}else {
			return user.getRole().getAuthorities().contains(getRolePost());
		}
	}
	
	private Authority getRolePost() {
		if(rolePost == null) {
			Optional<Authority> authority = authorityService.findByName("ROLE_POST");
			rolePost = authority.get();
		}
		
		return rolePost;
	}
}
