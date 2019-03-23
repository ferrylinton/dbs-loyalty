package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.dto.UserDto;
import com.dbs.loyalty.service.mapper.UserMapper;
import com.dbs.loyalty.service.specification.UserSpecification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService{
	
	private final ObjectMapper objectMapper;

	private final UserRepository userRepository;
	
	private final UserMapper userMapper;

	public Optional<UserDto> findByUsername(String username){
		return userRepository.findWithRoleByUsername(username).map(userMapper::toDto);
	}
	
	public Optional<UserDto> findById(String id) {
		return userRepository.findById(id).map(userMapper::toDto);
	}
	
	public Optional<UserDto> findWithRoleById(String id) {
		return userRepository.findWithRoleById(id).map(userMapper::toDto);
	}
	
	public Page<UserDto> findAll(Pageable pageable, HttpServletRequest request) {
		return userRepository.findAll(UserSpecification.getSpec(request), pageable).map(userMapper::toDto);
	}
	
	public boolean isEmailExist(UserDto userDto) {
		return isExist(userRepository.findByEmailIgnoreCase(userDto.getEmail()), userDto.getId());
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
	
	public String execute(TaskDto taskDto) throws JsonParseException, JsonMappingException, IOException {
		UserDto dto = objectMapper.readValue((taskDto.getTaskOperation() == TaskOperation.DELETE) ? taskDto.getTaskDataOld() : taskDto.getTaskDataNew(), UserDto.class);
		
		if(taskDto.isVerified()) {
			User user = userMapper.toEntity(dto);
			if(taskDto.getTaskOperation() == TaskOperation.ADD) {
				user.setCreatedBy(taskDto.getMaker());
				user.setCreatedDate(taskDto.getMadeDate());
				userRepository.save(user);
			}else if(taskDto.getTaskOperation() == TaskOperation.MODIFY) {
				user.setLastModifiedBy(taskDto.getMaker());
				user.setLastModifiedDate(taskDto.getMadeDate());
				userRepository.save(user);
			}else if(taskDto.getTaskOperation() == TaskOperation.DELETE) {
				userRepository.delete(user);
			}
		}

		return dto.getUsername();
	}
	
}
