package com.dbs.loyalty.service;

import static com.dbs.loyalty.config.constant.EntityConstant.CUSTOMER;
import static com.dbs.loyalty.config.constant.EntityConstant.PROMO;
import static com.dbs.loyalty.config.constant.EntityConstant.PROMO_CATEGORY;
import static com.dbs.loyalty.config.constant.EntityConstant.ROLE;
import static com.dbs.loyalty.config.constant.EntityConstant.USER;

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

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskOperation;
import com.dbs.loyalty.domain.enumeration.TaskStatus;
import com.dbs.loyalty.repository.TaskRepository;
import com.dbs.loyalty.service.dto.TaskDto;
import com.dbs.loyalty.service.mapper.TaskMapper;
import com.dbs.loyalty.service.specification.TaskSpecification;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskService {
	
	private String noServiceFormat = "No service for %s";

	private final ApplicationContext context;
	
	private final TaskRepository taskRepository;
	
	private final ObjectMapper objectMapper;
	
	private final TaskMapper taskMapper;

	public Optional<TaskDto> findById(String id) {
		return taskRepository.findById(id).map(taskMapper::toDto);
	}
	
	public Page<TaskDto> findAll(String taskDataType, Map<String, String> params, Pageable pageable, HttpServletRequest request){
		return taskRepository.findAll(TaskSpecification.getSpec(taskDataType, params, request), pageable).map(taskMapper::toDto);
	}
	
	public Task saveTaskAdd(String type, Object taskDataNew) throws JsonProcessingException {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.ADD);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(type);
		task.setTaskDataNew(objectMapper.writeValueAsString(taskDataNew));
		task.setMaker(SecurityUtil.getLogged());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	public Task saveTaskModify(String type, Object taskDataOld, Object taskDataNew) throws JsonProcessingException {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.MODIFY);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(type);
		task.setTaskDataOld(objectMapper.writeValueAsString(taskDataOld));
		task.setTaskDataNew(objectMapper.writeValueAsString(taskDataNew));
		task.setMaker(SecurityUtil.getLogged());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	public Task saveTaskDelete(String type, Object taskDataOld) throws JsonProcessingException {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.DELETE);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(type);
		task.setTaskDataOld(objectMapper.writeValueAsString(taskDataOld));
		task.setMaker(SecurityUtil.getLogged());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	@Transactional
	public String save(TaskDto taskDto) throws IOException {
		Task task = taskMapper.toEntity(taskDto);
		task.setTaskStatus(taskDto.isVerified() ? TaskStatus.VERIFIED : TaskStatus.REJECTED);
		task.setChecker(SecurityUtil.getLogged());
		task.setCheckedDate(Instant.now());
		
		taskRepository.save(task);
		
		System.out.println("TaskOperation : " + taskDto.getTaskOperation());
		
		if(taskDto.getTaskDataType().equals(ROLE)) {
			return context.getBean(RoleService.class).execute(taskDto);
		}else if(taskDto.getTaskDataType().equals(USER)) {
			return context.getBean(UserService.class).execute(taskDto);
		}else if(taskDto.getTaskDataType().equals(PROMO_CATEGORY)) {
			return context.getBean(PromoCategoryService.class).execute(taskDto);
		}else if(taskDto.getTaskDataType().equals(PROMO)) {
			return context.getBean(PromoService.class).execute(taskDto);
		}else if(taskDto.getTaskDataType().equals(CUSTOMER)) {
			return context.getBean(CustomerService.class).execute(taskDto);
		}
		
		return String.format(noServiceFormat, taskDto.getTaskDataType());
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
