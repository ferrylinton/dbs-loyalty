package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
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

	public Optional<Role> findById(String id){
		return roleRepository.findById(id);
	}
	
	public Optional<Role> findWithAuthoritiesById(String id){
		return roleRepository.findWithAuthoritiesById(id);
	}

	public List<Role> findAll(Sort sort){
		return roleRepository.findAll(sort);
	}
	
	public Page<Role> findAll(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}
	
	public Page<Role> findAll(String keyword, Pageable pageable) {
		if(keyword == null) {
			return findAll(pageable);
		}else {
			return roleRepository.findAllByNameContainingAllIgnoreCase(keyword, pageable);
		}
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

	public String execute(Task task) throws JsonParseException, JsonMappingException, IOException {
		Role role = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), Role.class);
		
		if(task.getTaskOperation() == TaskOperation.ADD) {
			role.setCreatedBy(task.getMaker());
			role.setCreatedDate(task.getMadeDate());
			roleRepository.save(role);
		}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
			role.setLastModifiedBy(task.getMaker());
			role.setLastModifiedDate(task.getMadeDate());
			roleRepository.save(role);
		}else if(task.getTaskOperation() == TaskOperation.DELETE) {
			roleRepository.delete(role);
		}
		
		return role.getName();
	}
	
}
