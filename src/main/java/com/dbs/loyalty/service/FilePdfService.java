package com.dbs.loyalty.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.FilePdf;
import com.dbs.loyalty.repository.FilePdfRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FilePdfService{
	
	private final FilePdfRepository filePdfRepository;
	
	public Optional<FilePdf> findById(String id){
		return filePdfRepository.findById(id);
	}
	
	public Optional<FilePdf> findOneByEventId(String id){
		return filePdfRepository.findOneByEventId(id);
	}
	
	public FilePdf save(MultipartFile file) throws IOException {
		FilePdf filePdf = new FilePdf();
		filePdf.setId(UUID.randomUUID().toString());
		filePdf.setBytes(file.getBytes());
		return filePdfRepository.save(filePdf);
	}

}
