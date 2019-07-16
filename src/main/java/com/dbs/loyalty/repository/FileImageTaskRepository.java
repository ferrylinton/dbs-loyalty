package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.file.FileImageTask;

public interface FileImageTaskRepository extends JpaRepository<FileImageTask, String>{

	
}
