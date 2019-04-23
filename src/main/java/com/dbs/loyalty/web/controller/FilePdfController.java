package com.dbs.loyalty.web.controller;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;

import java.util.Optional;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbs.loyalty.domain.FilePdf;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.FilePdfService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/pdf")
public class FilePdfController {

	private final FilePdfService filePdfService;
	
	@GetMapping("/{id}")
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
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, SecurityUtil.getLogged());
			throw new NotFoundException(message);
		}
	}
	
}
