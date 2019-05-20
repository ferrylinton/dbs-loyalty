package com.dbs.loyalty.web.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dbs.loyalty.domain.FilePdf;
import com.dbs.loyalty.domain.FilePdfTask;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.PdfService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PdfController extends AbstractController{

	private final PdfService filePdfService;
	
	@GetMapping("/pdf/{id}")
	public ResponseEntity<byte[]> getFilePdf(@PathVariable String id) throws NotFoundException {
		Optional<FilePdf> current = filePdfService.findById(id);
    	
		if(current.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.APPLICATION_PDF);
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(current.get().getBytes());
		}else {
			throw new NotFoundException(getNotFoundMessage(id));
		}
	}
	
	@GetMapping("/pdf/task/{id}")
	public ResponseEntity<byte[]> getFilePdfTask(@PathVariable String id) throws NotFoundException {
		Optional<FilePdfTask> current = filePdfService.findPdfTaskById(id);
    	
		if(current.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.APPLICATION_PDF);
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(current.get().getBytes());
		}else {
			throw new NotFoundException(getNotFoundMessage(id));
		}
	}
	
	@GetMapping("/pdf/download/{id}")
	public ResponseEntity<InputStreamResource> downloadFilePdf(@PathVariable String id) throws NotFoundException, IOException {
		Optional<FilePdf> current = filePdfService.findById(id);
    	
		if(current.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.add("content-disposition", "attachment;filename=" + id + ".pdf");
			ByteArrayResource resource = new ByteArrayResource(current.get().getBytes());
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(new InputStreamResource(resource.getInputStream()));
		}else {
			throw new NotFoundException(getNotFoundMessage(id));
		}
	}
	
}
