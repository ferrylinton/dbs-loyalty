package com.dbs.loyalty.web.controller;

import java.util.Optional;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbs.loyalty.domain.AbstractFileImage;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.util.MessageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/image")
public class ImageController {

	private final ImageService imageService;
	
	@GetMapping("/{id}")
	public ResponseEntity<byte[]> getFileImage(@PathVariable String id) throws NotFoundException {
		return getImage(imageService.findById(id), id);
	}
	
	@GetMapping("/task/{id}")
	public ResponseEntity<byte[]> getFileImageTask(@PathVariable String id) throws NotFoundException {
		return getImage(imageService.findImageTaskById(id), id);
	}
	
	private ResponseEntity<byte[]> getImage(Optional<? extends AbstractFileImage> image, String id) throws NotFoundException {
		if(image.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.valueOf(image.get().getContentType()));
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(image.get().getBytes());
		}else {
			throw new NotFoundException(MessageUtil.getNotFoundMessage(id));
		}
	}
	
}
