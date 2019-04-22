package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.FilePdf;
import com.dbs.loyalty.repository.FileImageRepository;
import com.dbs.loyalty.repository.FilePdfRepository;
import com.dbs.loyalty.util.ImageUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FilePdfService{
	
	private final FilePdfRepository filePdfRepository;
	
	public FilePdf save(MultipartFile file) throws IOException {
		FilePdf filePdf = new FilePdf();
		filePdf.setId(UUID.randomUUID().toString());
		filePdf.setBytes(file.getBytes());
		return filePdfRepository.save(filePdf);
	}


	public Optional<FilePdf> findOneByEventId(String id){
		return filePdfRepository.findOneByEventId(id);
	}
	
}
