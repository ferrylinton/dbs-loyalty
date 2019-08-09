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

import com.dbs.loyalty.config.constant.UserConstant;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.service.specification.UserSpec;
import com.dbs.loyalty.util.JsonUtil;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.TaskUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService{
	
	private static final String TYPE = User.class.getSimpleName();

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
	public void taskSave(User newData) throws JsonProcessingException {
		if(newData.getId() == null) {
			if(newData.getUserType().equals(UserConstant.EXTERNAL)) {
				newData.setPasswordHash(PasswordUtil.encode(newData.getPasswordPlain()));
				newData.setPasswordPlain(null);
			}
			
			taskService.saveTaskAdd(TYPE, JsonUtil.toString(newData));
		}else {
			User oldData = userRepository.getOne(newData.getId());
			
			if(newData.getUserType().equals(UserConstant.EXTERNAL)) {
				newData.setPasswordHash(oldData.getPasswordHash());
			}
			
			userRepository.save(true, newData.getId());
			taskService.saveTaskModify(TYPE, oldData.getId(), JsonUtil.toString(oldData), JsonUtil.toString(newData));
		}
	}

	@Transactional
	public void taskDelete(User oldData) throws JsonProcessingException {
		taskService.saveTaskDelete(TYPE, oldData.getId(), JsonUtil.toString(oldData));
		userRepository.save(true, oldData.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		User data = TaskUtil.getObject(task, User.class);
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				data.setCreatedBy(task.getMaker());
				data.setCreatedDate(task.getMadeDate());
				userRepository.save(data);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				data.setLastModifiedBy(task.getMaker());
				data.setLastModifiedDate(task.getMadeDate());
				data.setPending(false);
				userRepository.save(data);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				userRepository.delete(data);
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			userRepository.save(false, data.getId());
		}

		taskService.confirm(task);
		return data.getUsername();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Task task, String error) {
		try {
			User data = TaskUtil.getObject(task, User.class);
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				userRepository.save(false, data.getId());
			}

			taskService.save(task, error);
			return error;
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}
	
}
