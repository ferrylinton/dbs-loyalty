package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.Task;

public interface TaskRepository extends JpaRepository<Task, String>{

}
