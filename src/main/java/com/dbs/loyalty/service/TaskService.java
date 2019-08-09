package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.enumeration.TaskOperation;
import com.dbs.loyalty.enumeration.TaskStatus;
import com.dbs.loyalty.repository.TaskRepository;
import com.dbs.loyalty.service.specification.TaskSpec;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TaskService {

	private final TaskRepository taskRepository;

	public Optional<Task> findById(String id) {
		return taskRepository.findById(id);
	}
	
	public Page<Task> findAll(String taskDataType, Map<String, String> params, Pageable pageable){
		return taskRepository.findAll(new TaskSpec(taskDataType, params), pageable);
	}
	
	public Task saveTaskAdd(String taskDataType, String taskDataNew) {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.ADD);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(taskDataType);
		task.setTaskDataNew(taskDataNew);
		task.setMaker(SecurityUtil.getLogged());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	public Task saveTaskModify(String taskDataType, String taskDataId, String taskDataOld, String taskDataNew) {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.MODIFY);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(taskDataType);
		task.setTaskDataId(taskDataId);
		task.setTaskDataOld(taskDataOld);
		task.setTaskDataNew(taskDataNew);
		task.setMaker(SecurityUtil.getLogged());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	public Task saveTaskDelete(String taskDataType, String taskDataId, String taskDataOld) {
		Task task = new Task();
		task.setTaskOperation(TaskOperation.DELETE);
		task.setTaskStatus(TaskStatus.PENDING);
		task.setTaskDataType(taskDataType);
		task.setTaskDataId(taskDataId);
		task.setTaskDataOld(taskDataOld);
		task.setMaker(SecurityUtil.getLogged());
		task.setMadeDate(Instant.now());
		return taskRepository.save(task);
	}
	
	public void confirm(Task task) {
		task.setTaskStatus(task.getVerified() ? TaskStatus.VERIFIED : TaskStatus.REJECTED);
		task.setChecker(SecurityUtil.getLogged());
		task.setCheckedDate(Instant.now());
		
		taskRepository.save(task);
	}

	public void save(Task task, String error) {
		task.setTaskStatus(TaskStatus.FAILED);
		task.setError(error);
		taskRepository.save(task);
	}
	
}
