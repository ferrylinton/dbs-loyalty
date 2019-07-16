package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.service.specification.UserSpec;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService{
	
	private final ObjectMapper objectMapper;

	private final UserRepository userRepository;

	public Optional<User> findByUsername(String username){
		return userRepository.findWithRoleByUsername(username);
	}
	
	public Optional<User> findById(String id) {
		return userRepository.findById(id);
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
	
	public String execute(Task task) throws IOException {
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
	
}
