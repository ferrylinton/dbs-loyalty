package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.file.FilePdf;
import com.dbs.loyalty.domain.file.FilePdfTask;
import com.dbs.loyalty.repository.FilePdfRepository;
import com.dbs.loyalty.repository.FilePdfTaskRepository;
import com.dbs.loyalty.util.SecurityUtil;
import com.devskiller.friendly_id.FriendlyId;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PdfService{
	
	private final FilePdfRepository filePdfRepository;
	
	private final FilePdfTaskRepository filePdfTaskRepository;
	
	public Optional<FilePdf> findById(String id){
		return filePdfRepository.findById(id);
	}
	
	public Optional<FilePdfTask> findPdfTaskById(String id){
		return filePdfTaskRepository.findById(id);
	}
	
	public Optional<FilePdf> findOneByEventId(String id){
		return filePdfRepository.findOneByEventId(id);
	}
	
	public FilePdfTask add(MultipartFile file) throws IOException {
		FilePdfTask fileTaskPdf = new FilePdfTask();
		fileTaskPdf.setId(FriendlyId.createFriendlyId());
		fileTaskPdf.setBytes(file.getBytes());
		fileTaskPdf.setCreatedBy(SecurityUtil.getLogged());
		fileTaskPdf.setCreatedDate(Instant.now());
		return filePdfTaskRepository.save(fileTaskPdf);
	}

	public FilePdf add(String id, String filePdfTaskId, String createdBy, Instant createdDate) {
		Optional<FilePdfTask> filePdfTask = filePdfTaskRepository.findById(filePdfTaskId);
		
		if(filePdfTask.isPresent()) {
			FilePdf filePdf = new FilePdf();
			filePdf.setId(id);
			filePdf.setBytes(filePdfTask.get().getBytes());
			filePdf.setCreatedBy(createdBy);
			filePdf.setCreatedDate(createdDate);
			
			return filePdfRepository.save(filePdf);
		}

		return null;
	}
	
	public FilePdf update(String id, String filePdfTaskId, String lastModifiedBy, Instant lastModifiedDate) throws IOException {
		Optional<FilePdfTask> filePdfTask = filePdfTaskRepository.findById(filePdfTaskId);
		
		if(filePdfTask.isPresent()) {
			Optional<FilePdf> filePdf = filePdfRepository.findById(id);
			
			if(filePdf.isPresent()) {
				filePdf.get().setBytes(filePdfTask.get().getBytes());
				filePdf.get().setLastModifiedBy(lastModifiedBy);
				filePdf.get().setLastModifiedDate(lastModifiedDate);
				
				return filePdfRepository.save(filePdf.get());
			}
		}

		return null;
	}

	public void delete(String id){
		filePdfRepository.deleteById(id);
	}
	
}
