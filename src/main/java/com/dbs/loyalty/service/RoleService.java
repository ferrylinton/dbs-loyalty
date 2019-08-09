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

import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.repository.RoleRepository;
import com.dbs.loyalty.service.specification.RoleSpec;
import com.dbs.loyalty.util.JsonUtil;
import com.dbs.loyalty.util.SortUtil;
import com.dbs.loyalty.util.TaskUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService{

	private static final String TYPE = Role.class.getSimpleName();

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
	public void taskSave(Role newData) throws JsonProcessingException {
		if(newData.getId() == null) {
			taskService.saveTaskAdd(TYPE, JsonUtil.toString(newData));
		}else {
			Role oldRole = roleRepository.findWithAuthoritiesById(newData.getId());
			roleRepository.save(true, newData.getId());
			taskService.saveTaskModify(TYPE, oldRole.getId(), JsonUtil.toString(oldRole), JsonUtil.toString(newData));
		}
	}

	@Transactional
	public void taskDelete(Role oldData) throws JsonProcessingException {
		taskService.saveTaskDelete(TYPE, oldData.getId(), JsonUtil.toString(oldData));
		roleRepository.save(true, oldData.getId());
	}
	
	@Transactional
	public String taskConfirm(Task task) throws IOException {
		Role role = TaskUtil.getObject(task, Role.class);
		
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

		taskService.confirm(task);
		return role.getName();
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public String taskFailed(Task task, String error) {
		try {
			if(task.getTaskDataId() != null) {
				roleRepository.save(false, task.getTaskDataId());
			}

			taskService.save(task, error);
			return error;
		} catch (Exception ex) {
			return ex.getLocalizedMessage();
		}
	}
	
}
