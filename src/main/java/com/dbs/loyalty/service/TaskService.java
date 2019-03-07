package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.domain.enumeration.TaskStatus;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.TaskRepository;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaskService {

	private final Logger LOG = LoggerFactory.getLogger(TaskService.class);

	private final ApplicationContext context;
	
	private final TaskRepository taskRepository;
	
	private final ObjectMapper objectMapper;
	
	public TaskService(ApplicationContext context, TaskRepository taskRepository, ObjectMapper objectMapper) {
		this.context = context;
		this.taskRepository = taskRepository;
		this.objectMapper = objectMapper;
	}
	
	public Optional<Task> findById(String id) throws NotFoundException {
		Optional<Task> task = taskRepository.findById(id);

		if (task.isPresent()) {
			return task;
		} else {
			throw new NotFoundException();
		}
	}
	
	public Page<Task> findAll(Pageable pageable){
		return taskRepository.findAll(pageable);
	}
	
	public Task saveTaskAdd(Object taskDataNew) throws JsonProcessingException {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.ADD);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(taskDataNew.getClass().getSimpleName());
		task.setTaskDataNew(objectMapper.writeValueAsString(taskDataNew));
		task.setMaker(SecurityUtil.getCurrentEmail());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	public Task saveTaskModify(Object taskDataOld, Object taskDataNew) throws JsonProcessingException {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.MODIFY);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(taskDataOld.getClass().getSimpleName());
		task.setTaskDataOld(objectMapper.writeValueAsString(taskDataOld));
		task.setTaskDataNew(objectMapper.writeValueAsString(taskDataNew));
		task.setMaker(SecurityUtil.getCurrentEmail());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	public Task saveTaskDelete(Object taskDataOld) throws JsonProcessingException {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.DELETE);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(taskDataOld.getClass().getSimpleName());
		task.setTaskDataOld(objectMapper.writeValueAsString(taskDataOld));
		task.setMaker(SecurityUtil.getCurrentEmail());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	@Transactional
	public String save(Task task) throws JsonParseException, JsonMappingException, IOException {
		taskRepository.save(task);
		
		if(task.getTaskDataType().equals(Role.class.getSimpleName())) {
			return context.getBean(RoleService.class).execute(task);
		}
		
		return Constant.EMPTY;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void save(Exception ex, Task task) {
		try {
			task.setTaskStatus(TaskStatus.FAILED);
			task.setError(ex.getLocalizedMessage());
			task.setErrorDetail(ErrorUtil.getErrorMessage(ex));
			taskRepository.save(task);
		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage(), e);
		}
	}

}
