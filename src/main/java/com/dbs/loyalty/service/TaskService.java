package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.domain.enumeration.TaskStatus;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.TaskRepository;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TaskService {

	private final String ENTITY_NAME = "task";
	
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
	
	public Task save(TaskOperation taskOperation, String taskDataType, Object value) throws JsonProcessingException {
		Task task = new Task();
		task.setTaskOperation(taskOperation);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(taskDataType);
		task.setTaskData(objectMapper.writeValueAsString(value));
		task.setMaker(SecurityUtil.getCurrentEmail());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	public Task save(Task task) throws JsonParseException, JsonMappingException, IOException {
		if(task.getTaskDataType().equals(Role.class.getSimpleName())) {
			context.getBean(RoleService.class).execute(task);
		}
		
		return taskRepository.save(task);
	}
	
	public void viewForm(ModelMap model, String id) throws NotFoundException {
		if (id.equals(Constant.ZERO)) {
			model.addAttribute(ENTITY_NAME, new Task());
		} else {
			Optional<Task> task = findById(id);
			model.addAttribute(ENTITY_NAME, task.get());
		}
	}

}
