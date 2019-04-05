package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService{

	private Sort sortByName = Sort.by("name");
	
	private final ObjectMapper objectMapper;
	
	private final RoleRepository roleRepository;
	
	private final RoleMapper roleMapper;

	public Optional<RoleDto> findById(String id){
		return roleRepository
				.findById(id)
				.map(roleMapper::toDto);
	}
	
	public Optional<RoleDto> findByName(String name) {
		return roleRepository
				.findByNameIgnoreCase(name)
				.map(roleMapper::toDto);
	}
	
	public Optional<RoleDto> findWithAuthoritiesById(String id){
		return roleRepository
				.findWithAuthoritiesById(id)
				.map(roleMapper::toDtoWithAuthorities);
	}

	public List<RoleDto> findAll(){
		return roleMapper.toDto(roleRepository.findAll(sortByName));
	}
	
	public Page<RoleDto> findAll(Pageable pageable, HttpServletRequest request) {
		return roleRepository
				.findAll(RoleSpecification.getSpec(request), pageable)
				.map(roleMapper::toDto);
	}

	public boolean isNameExist(RoleDto roleDto) {
		Optional<Role> role = roleRepository.findByNameIgnoreCase(roleDto.getName());

		if (role.isPresent()) {
			return (roleDto.getId() == null) || (!roleDto.getId().equals(role.get().getId()));
		}else {
			return false;
		}
	}

	public String execute(TaskDto taskDto) throws IOException {
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
