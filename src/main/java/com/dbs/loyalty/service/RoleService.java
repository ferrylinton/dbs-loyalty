package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.RoleRepository;
import com.dbs.loyalty.service.specification.RoleSpec;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService{

	public static final Sort SORT_BY = Sort.by(DomainConstant.NAME);
	
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
		return roleRepository.findAll(SORT_BY);
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

	public Role save(Role role) {
		return roleRepository.save(role);
	}
	
	public void save(boolean pending, String id) {
		roleRepository.save(pending, id);
	}
	
	@Transactional
	public void taskSave(Role role) throws JsonProcessingException {
		if(role.getId() == null) {
			taskService.saveTaskAdd(DomainConstant.ROLE, role);
		}else {
			taskService.saveTaskModify(DomainConstant.ROLE, roleRepository.findWithAuthoritiesById(role.getId()), role);
			save(true, role.getId());
		}
	}

	@Transactional
	public void taskDelete(Role role) throws JsonProcessingException {
		taskService.saveTaskDelete(DomainConstant.ROLE, role);
		save(true, role.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		taskService.confirm(task);
		Role role = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), Role.class);
		
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
			Role role = objectMapper.readValue((task.getTaskOperation() == TaskOperation.DELETE) ? task.getTaskDataOld() : task.getTaskDataNew(), Role.class);
			
			if(task.getTaskOperation() != TaskOperation.ADD) {
				roleRepository.save(false, role.getId());
			}

			taskService.save(ex, task);
			return ex.getLocalizedMessage();
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
	}
	
}
