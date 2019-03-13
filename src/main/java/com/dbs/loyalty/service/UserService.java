package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.service.specification.UserSpecification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class UserService{
	
	private final ObjectMapper objectMapper;

	private final UserRepository userRepository;
 
	public UserService(ObjectMapper objectMapper, UserRepository userRepository) {
		this.objectMapper = objectMapper;
		this.userRepository = userRepository;
	}
	
	public Optional<User> findByEmail(String email){
		return userRepository.findByEmail(email);
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

		return user.getName();
	}
	
}
