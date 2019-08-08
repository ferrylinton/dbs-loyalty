package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.RoleRepository;
import com.dbs.loyalty.service.specification.RoleSpec;
import com.dbs.loyalty.util.SortUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService{

	private final ObjectMapper objectMapper;
	
	private final RoleRepository roleRepository;
	
	private final TaskService taskService;

	public Optional<Role> findById(String id){
		return roleRepository.findById(id);
	}
	
	public Optional<Role> findByName(String name) {
		return roleRepository.findByNameIgnoreCase(name);
	}
	
	public Role findWithAuthoritiesById(String id){
		return roleRepository.findWithAuthoritiesById(id);
	}

	public List<Role> findAll(){
		return roleRepository.findAll(SortUtil.SORT_BY_NAME);
	}
	
	public Page<Role> findAll(Map<String, String> params, Pageable pageable) {
		return roleRepository.findAll(new RoleSpec(params), pageable);
	}

	public boolean isNameExist(String id, String name) {
		Optional<Role> role = roleRepository.findByNameIgnoreCase(name);

		if (role.isPresent()) {
			return (id == null) || (!id.equals(role.get().getId()));
		}else {
			return false;
		}
	}

	@Transactional
	public void taskSave(Role newRole) throws JsonProcessingException {
		if(newRole.getId() == null) {
			taskService.saveTaskAdd(DomainConstant.ROLE, toString(newRole));
		}else {
			Role oldRole = roleRepository.findWithAuthoritiesById(newRole.getId());
			roleRepository.save(true, newRole.getId());
			taskService.saveTaskModify(DomainConstant.ROLE, toString(oldRole), toString(newRole));
		}
	}

	@Transactional
	public void taskDelete(Role role) throws JsonProcessingException {
		taskService.saveTaskDelete(DomainConstant.ROLE, toString(role));
		roleRepository.save(true, role.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		taskService.confirm(task);
		Role role = toRole((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew());
		
		if(task.getVerified()) {
			if(task.getTaskOperation() == TaskOperation.ADD) {
				role.setCreatedBy(task.getMaker());
				role.setCreatedDate(task.getMadeDate());
				roleRepository.save(role);
			}else if(task.getTaskOperation() == TaskOperation.MODIFY) {
				role.setLastModifiedBy(task.getMaker());
				role.setLastModifiedDate(task.getMadeDate());
				role.setPending(false);
				roleRepository.save(role);
			}else if(task.getTaskOperation() == TaskOperation.DELETE) {
				roleRepository.delete(role);
			}
		}else if(task.getTaskOperation() != TaskOperation.ADD) {
			roleRepository.save(false, role.getId());
		}

		return role.getName();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Exception ex, Task task) {
		try {
			Role role = toRole((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew());
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				roleRepository.save(false, role.getId());
			}

			taskService.save(ex, task);
			return ex.getLocalizedMessage();
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
	private String toString(Role role) throws JsonProcessingException {
		return objectMapper.writeValueAsString(role);
	}
	
	private Role toRole(String content) throws IOException {
		return objectMapper.readValue(content, Role.class);
	}
	
}
