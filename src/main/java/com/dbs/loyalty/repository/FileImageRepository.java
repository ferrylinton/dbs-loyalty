package com.dbs.loyalty.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dbs.loyalty.domain.file.FileImage;

public interface FileImageRepository extends JpaRepository<FileImage, String>{
	
}
