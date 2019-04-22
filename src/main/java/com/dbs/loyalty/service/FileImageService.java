package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.repository.FileImageRepository;
import com.dbs.loyalty.util.ImageUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FileImageService{
	
	private final FileImageRepository fileImageRepository;
	
	public FileImage save(MultipartFile file) throws IOException {
		FileImage fileImage = new FileImage();
		fileImage.setId(UUID.randomUUID().toString());
		fileImage.setLastModifiedBy(SecurityUtil.getLogged());
		fileImage.setLastModifiedDate(Instant.now());
		ImageUtil.setFileImage(fileImage, file);
		fileImage = fileImageRepository.save(fileImage);
		return fileImage;
	}

	public Optional<FileImage> findOneByCustomerEmail(String email){
		return fileImageRepository.findOneByCustomerEmail(email);
	}

	public Optional<FileImage> findOneByPromoId(String id){
		return fileImageRepository.findOneByPromoId(id);
	}
	
	public Optional<FileImage> findOneByEventId(String id){
		return fileImageRepository.findOneByEventId(id);
	}
	
}
