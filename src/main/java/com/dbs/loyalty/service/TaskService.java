package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.domain.enumeration.TaskStatus;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.repository.TaskRepository;
import com.dbs.loyalty.service.dto.PromoCategoryDto;
import com.dbs.loyalty.service.dto.PromoDto;
import com.dbs.loyalty.service.dto.RoleDto;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.dto.UserDto;
import com.dbs.loyalty.service.mapper.TaskMapper;
import com.dbs.loyalty.service.specification.TaskSpecification;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {

	private final ApplicationContext context;
	
	private final TaskRepository taskRepository;
	
	private final ObjectMapper objectMapper;
	
	private final TaskMapper taskMapper;

	public Optional<TaskDto> findById(String id) throws NotFoundException {
		return taskRepository.findById(id).map(taskMapper::toDto);
	}
	
	public Page<TaskDto> findAll(String taskDataType, Map<String, String> params, Pageable pageable, HttpServletRequest request){
		return taskRepository.findAll(TaskSpecification.getSpec(taskDataType, params, request), pageable).map(taskMapper::toDto);
	}
	
	public Task saveTaskAdd(Object taskDataNew) throws JsonProcessingException {
		String temp = objectMapper.writeValueAsString(taskDataNew);
		System.out.println(temp);
		try {
			RoleDto roleDto = objectMapper.readValue(temp, RoleDto.class);
			System.out.println(roleDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
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
	public String save(TaskDto taskDto) throws JsonParseException, JsonMappingException, IOException {
		Task task = taskMapper.toEntity(taskDto);
		taskRepository.save(task);
		
		if(taskDto.getTaskDataType().equals(RoleDto.class.getSimpleName())) {
			return context.getBean(RoleService.class).execute(taskDto);
		}else if(taskDto.getTaskDataType().equals(UserDto.class.getSimpleName())) {
			return context.getBean(UserService.class).execute(taskDto);
		}else if(taskDto.getTaskDataType().equals(PromoCategoryDto.class.getSimpleName())) {
			return context.getBean(PromoCategoryService.class).execute(taskDto);
		}else if(taskDto.getTaskDataType().equals(PromoDto.class.getSimpleName())) {
			return context.getBean(PromoService.class).execute(taskDto);
		}
		
		return Constant.EMPTY;
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void save(Exception ex, TaskDto taskDto) {
		try {
			Task task = taskMapper.toEntity(taskDto);
			task.setTaskStatus(TaskStatus.FAILED);
			task.setError(StringUtils.substring(ex.getLocalizedMessage(), 0, 250));
			task.setErrorDetail(ErrorUtil.getErrorMessage(ex));
			taskRepository.save(task);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}

}
