package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Authority;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.service.specification.UserSpecification;
import com.dbs.loyalty.util.PasswordUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService{
	
	private final ApplicationProperties applicationProperties;
	
	private final ObjectMapper objectMapper;

	private final UserRepository userRepository;
 
	public UserService(ApplicationProperties applicationProperties, ObjectMapper objectMapper, UserRepository userRepository) {
		this.applicationProperties = applicationProperties;
		this.objectMapper = objectMapper;
		this.userRepository = userRepository;
	}
	
	public Optional<User> findWithRoleByUsername(String username){
		return userRepository.findWithRoleByUsername(username);
	}
	
	public Optional<User> findById(String id) {
		return userRepository.findById(id);
	}
	
	public Optional<User> findWithRoleById(String id) {
		return userRepository.findWithRoleById(id);
	}
	
	public Page<User> findAll(Pageable pageable, HttpServletRequest request) {
		return userRepository.findAll(UserSpecification.getSpec(request), pageable);
	}
	
	public boolean isEmailExist(User user) {
		return isExist(userRepository.findByEmailIgnoreCase(user.getEmail()), user.getId());
	}
	
	private boolean isExist(Optional<User> user, String id) {
		if(user.isPresent()) {
			if(id == null) {
				return true;
			}else if(!id.equals(user.get().getId())) {
				return true;
			}
		}
		
		return false;
	}
	
	public String execute(Task task) throws JsonParseException, JsonMappingException, IOException {
		User user = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), User.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				user.setCreatedBy(task.getMaker());
				user.setCreatedDate(task.getMadeDate());
				userRepository.save(user);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				user.setLastModifiedBy(task.getMaker());
				user.setLastModifiedDate(task.getMadeDate());
				userRepository.save(user);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				userRepository.delete(user);
			}
		}

		return user.getUsername();
	}
	
	public Authentication authenticate(User user, String password) {
		if(!user.getActivated()) {
    		throw new LockedException(Constant.EMPTY);
		}else if(user.getLoginAttemptCount() >= applicationProperties.getSecurity().getLoginAttemptCount()) {
			throw new LockedException(Constant.EMPTY);
    	}else if(PasswordUtil.getInstance().matches(password, user.getPasswordHash())) {
    		resetLoginAttemptCount(user.getUsername());
    		return new UsernamePasswordAuthenticationToken(user.getUsername(), password, getAuthorities(user));
        }else {
        	addLoginAttemptCount(user.getLoginAttemptCount(), user.getUsername());
        	throw new BadCredentialsException(Constant.EMPTY);
        }
	}
	
	public void addLoginAttemptCount(Integer loginAttemptCount, String username) {
		loginAttemptCount = (loginAttemptCount < 3) ? (loginAttemptCount + 1) : loginAttemptCount;
		userRepository.updateLoginAttemptCount(loginAttemptCount, username);
	}

	public void resetLoginAttemptCount(String username) {
		userRepository.updateLoginAttemptCount(0, username);
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new HashSet<>();
		
		for(Authority authority : user.getRole().getAuthorities()){
			authorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
		
        return authorities;
    }
}
