package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.repository.RoleRepository;
import com.dbs.loyalty.service.dto.RoleDto;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.mapper.RoleMapper;
import com.dbs.loyalty.service.specification.RoleSpecification;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService{

	private final static Sort SORT_BY_NAME = Sort.by("name");
	
	private final ObjectMapper objectMapper;
	
	private final RoleRepository roleRepository;
	
	private final RoleMapper roleMapper;

	public Optional<RoleDto> findById(String id){
		return roleRepository.findById(id).map(roleMapper::toDto);
	}
	
	public Optional<RoleDto> findByName(String name) {
		return roleRepository.findByNameIgnoreCase(name).map(roleMapper::toDto);
	}
	
	public Optional<RoleDto> findWithAuthoritiesById(String id){
		return roleRepository.findWithAuthoritiesById(id).map(roleMapper::toDto);
	}
	
	public Optional<RoleDto> findWithAuthoritiesByName(String name){
		return roleRepository.findWithAuthoritiesByName(name).map(roleMapper::toDto);
	}

	public List<RoleDto> findAll(){
		return roleRepository.findAll(SORT_BY_NAME)
				.stream()
				.map(roleMapper::toDto)
				.collect(Collectors.toList());
	}
	
	public Page<RoleDto> findAll(Pageable pageable, HttpServletRequest request) {
		return roleRepository.findAll(RoleSpecification.getSpec(request), pageable)
				.map(roleMapper::toDto);
	}

	public boolean isNameExist(RoleDto roleDto) {
		Optional<Role> obj = roleRepository.findByNameIgnoreCase(roleDto.getName());

		if (obj.isPresent()) {
			if (roleDto.getId() == null) {
				return true;
			} else if (!roleDto.getId().equals(obj.get().getId())) {
				return true;
			}
		}

		return false;
	}

	public String execute(TaskDto taskDto) throws JsonParseException, JsonMappingException, IOException {
		RoleDto roleDto = objectMapper.readValue((taskDto.getTaskOperation() == TaskOperation.DELETE) ? taskDto.getTaskDataOld() : taskDto.getTaskDataNew(), RoleDto.class);
		
		if(taskDto.isVerified()) {
			Role role = roleMapper.toEntity(roleDto);
			
			if(taskDto.getTaskOperation() == TaskOperation.ADD) {
				role.setCreatedBy(taskDto.getMaker());
				role.setCreatedDate(taskDto.getMadeDate());
				roleRepository.save(role);
			}else if(taskDto.getTaskOperation() == TaskOperation.MODIFY) {
				role.setLastModifiedBy(taskDto.getMaker());
				role.setLastModifiedDate(taskDto.getMadeDate());
				roleRepository.save(role);
			}else if(taskDto.getTaskOperation() == TaskOperation.DELETE) {
				roleRepository.delete(role);
			}
		}

		return roleDto.getName();
	}
	
}
