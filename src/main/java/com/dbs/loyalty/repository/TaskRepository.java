package com.dbs.loyalty.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dbs.loyalty.domain.Task;
import com.dbs.loyalty.domain.enumeration.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, String>, JpaSpecificationExecutor<Task>{

	Page<Task> findByTaskStatus(TaskStatus taskStatus, Pageable pageable);
	
	
}
