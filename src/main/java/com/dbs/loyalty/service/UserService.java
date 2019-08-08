package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.UserTypeConstant;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.service.specification.UserSpec;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService{
	
	private final ObjectMapper objectMapper;

	private final UserRepository userRepository;
	
	private final TaskService taskService;

	public Optional<User> findByUsername(String username){
		return userRepository.findWithRoleByUsername(username);
	}
	
	public Optional<User> findById(String id) {
		return userRepository.findById(id);
	}
	
	public User getOne(String id) {
		return userRepository.getOne(id);
	}
	
	public Optional<User> findWithRoleById(String id) {
		return userRepository.findWithRoleById(id);
	}
	
	public boolean isUsernameExist(String id, String username) {
		Optional<User> user = userRepository.findByUsernameIgnoreCase(username);

		if (user.isPresent()) {
			return (id == null) || (!id.equals(user.get().getId()));
		}else {
			return false;
		}
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
	
	public void save(boolean pending, String id) {
		userRepository.save(pending, id);
	}
	
	public Optional<User> save(String username, String passwordHash) {
		User result = null;
		Optional<User> user = userRepository.findByUsernameIgnoreCase(username);
		
		if(user.isPresent()) {
			user.get().setPasswordHash(passwordHash);
			user.get().setLastModifiedBy(SecurityUtil.getLogged());
			user.get().setLastModifiedDate(Instant.now());
			result = userRepository.save(user.get());
		}

		return Optional.of(result);
	}
	
	public Page<User> findAll(Map<String, String> params, Pageable pageable) {
		return userRepository.findAll(new UserSpec(params), pageable);
	}
	
	@Transactional
	public void taskSave(User newUser) throws JsonProcessingException {
		if(newUser.getId() == null) {
			if(newUser.getUserType().equals(UserTypeConstant.EXTERNAL)) {
				newUser.setPasswordHash(PasswordUtil.encode(newUser.getPasswordPlain()));
				newUser.setPasswordPlain(null);
			}
			
			taskService.saveTaskAdd(DomainConstant.ROLE, toString(newUser));
		}else {
			User oldUser = userRepository.getOne(newUser.getId());
			
			if(newUser.getUserType().equals(UserTypeConstant.EXTERNAL)) {
				newUser.setPasswordHash(oldUser.getPasswordHash());
			}
			
			userRepository.save(true, newUser.getId());
			taskService.saveTaskModify(DomainConstant.ROLE, toString(oldUser), toString(newUser));
		}
	}

	@Transactional
	public void taskDelete(User user) throws JsonProcessingException {
		taskService.saveTaskDelete(DomainConstant.ROLE, toString(user));
		userRepository.save(true, user.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		taskService.confirm(task);
		User user = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), User.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				user.setCreatedBy(task.getMaker());
				user.setCreatedDate(task.getMadeDate());
				userRepository.save(user);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				user.setLastModifiedBy(task.getMaker());
				user.setLastModifiedDate(task.getMadeDate());
				user.setPending(false);
				userRepository.save(user);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				userRepository.delete(user);
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			userRepository.save(false, user.getId());
		}

		return user.getUsername();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Exception ex, Task task) {
		try {
			User user = toUser((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew());
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				userRepository.save(false, user.getId());
			}

			taskService.save(ex, task);
			return ex.getLocalizedMessage();
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	private String toString(User user) throws JsonProcessingException {
		return objectMapper.writeValueAsString(user);
	}
	
	private User toUser(String content) throws IOException {
		return objectMapper.readValue(content, User.class);
	}
	
}
