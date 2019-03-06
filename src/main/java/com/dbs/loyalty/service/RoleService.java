package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.RoleRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RoleService{

	private final ObjectMapper objectMapper;
	
	private final RoleRepository roleRepository;
	
	public RoleService(ObjectMapper objectMapper, RoleRepository roleRepository) {
		this.objectMapper = objectMapper;
		this.roleRepository = roleRepository;
	}

	public Optional<Role> findById(String id) throws NotFoundException {
		return roleRepository.findById(id);
	}

	public Page<Role> findAll(String keyword, Pageable pageable) {
		if (keyword == null || keyword.equals(Constant.EMPTY)) {
			return roleRepository.findAll(pageable);
		} else {
			return roleRepository.findAllByNameContainingAllIgnoreCase(keyword.trim(), pageable);
		}
	}
	
	public List<Role> findAll(){
		return roleRepository.findAll();
	}

	public Optional<Role> findByName(String name) {
		return roleRepository.findByNameIgnoreCase(name);
	}

	public boolean isNameExist(Role role) {
		Optional<Role> obj = roleRepository.findByNameIgnoreCase(role.getName());

		if (obj.isPresent()) {
			if (role.getId() == null) {
				return true;
			} else if (!role.getId().equals(obj.get().getId())) {
				return true;
			}
		}

		return false;
	}

	public Role save(Role role) {
		return roleRepository.save(role);
	}

	public void delete(Role role) throws NotFoundException {
		roleRepository.delete(role);
	}

	public Role execute(Task task) throws JsonParseException, JsonMappingException, IOException {
		Role role = objectMapper.readValue(task.getTaskData(), Role.class);
		
		if(task.getTaskOperation() == TaskOperation.ADD) {
			return roleRepository.save(role);
		}
		
		return role;
	}
}
