package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.file.FilePdfTask;

public interface FilePdfTaskRepository extends JpaRepository<FilePdfTask, String>{

	
}
