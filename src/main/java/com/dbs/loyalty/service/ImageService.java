package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.FileImageTask;
import com.dbs.loyalty.repository.FileImageRepository;
import com.dbs.loyalty.repository.FileImageTaskRepository;
import com.dbs.loyalty.util.ImageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.devskiller.friendly_id.FriendlyId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService{
	
	private final FileImageRepository fileImageRepository;
	
	private final FileImageTaskRepository fileImageTaskRepository;
	
	public Optional<FileImage> findById(String id){
		return fileImageRepository.findById(id);
	}
	
	public Optional<FileImageTask> findImageTaskById(String id){
		return fileImageTaskRepository.findById(id);
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
	
	public FileImageTask add(MultipartFile file) throws IOException {
		FileImageTask fileTaskImage = new FileImageTask();
		fileTaskImage.setId(FriendlyId.createFriendlyId());
		fileTaskImage.setCreatedBy(SecurityUtil.getLogged());
		fileTaskImage.setCreatedDate(Instant.now());
		ImageUtil.setFileImage(fileTaskImage, file);
		return fileImageTaskRepository.save(fileTaskImage);
	}
	
	public FileImage updateCustomerImage(MultipartFile file) throws IOException {
		Optional<FileImage> fileImage = fileImageRepository.findOneByCustomerEmail(SecurityUtil.getLogged());
		
		if(fileImage.isPresent()) {
			fileImage.get().setLastModifiedBy(SecurityUtil.getLogged());
			fileImage.get().setLastModifiedDate(Instant.now());
			ImageUtil.setFileImage(fileImage.get(), file);
			return fileImageRepository.save(fileImage.get());
		}

		return null;
	}

	public FileImage add(String id, String fileImageTaskId, String createdBy, Instant createdDate) {
		Optional<FileImageTask> fileImageTask = fileImageTaskRepository.findById(fileImageTaskId);
		
		if(fileImageTask.isPresent()) {
			FileImage fileImage = new FileImage();
			fileImage.setId(id);
			fileImage.setContentType(fileImageTask.get().getContentType());
			fileImage.setBytes(fileImageTask.get().getBytes());
			fileImage.setWidth(fileImageTask.get().getWidth());
			fileImage.setHeight(fileImageTask.get().getHeight());
			fileImage.setCreatedBy(createdBy);
			fileImage.setCreatedDate(createdDate);
			
			return fileImageRepository.save(fileImage);
		}

		return null;
	}
	
	public FileImage update(String id, String fileImageTaskId, String lastModifiedBy, Instant lastModifiedDate) throws IOException {
		Optional<FileImageTask> fileImageTask = fileImageTaskRepository.findById(fileImageTaskId);
		
		if(fileImageTask.isPresent()) {
			Optional<FileImage> fileImage = fileImageRepository.findById(id);
			
			if(fileImage.isPresent()) {
				fileImage.get().setContentType(fileImageTask.get().getContentType());
				fileImage.get().setBytes(fileImageTask.get().getBytes());
				fileImage.get().setWidth(fileImageTask.get().getWidth());
				fileImage.get().setHeight(fileImageTask.get().getHeight());
				fileImage.get().setLastModifiedBy(lastModifiedBy);
				fileImage.get().setLastModifiedDate(lastModifiedDate);
				
				return fileImageRepository.save(fileImage.get());
			}
		}

		return null;
	}

	public void delete(String id){
		fileImageRepository.deleteById(id);
	}
	
}
