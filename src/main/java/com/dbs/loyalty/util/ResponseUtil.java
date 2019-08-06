package com.dbs.loyalty.util;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.FilePdf;

public final class ResponseUtil {
	
	public static final String RESULT_FORMAT = "{\"message\":\"%s\"}";

	public static ResponseEntity<byte[]> createImageResponse(FileImage fileImage){
    	return ResponseEntity
				.ok()
				.headers(HeaderUtil.geImageHeaders(fileImage))
				.body(fileImage.getBytes());
    }
	
	public static ResponseEntity<InputStreamResource> createResponse(FilePdf filePdf) throws IOException{
		ByteArrayResource resource = new ByteArrayResource(filePdf.getBytes());
		
		return ResponseEntity
				.ok()
				.headers(HeaderUtil.gePdfHeaders(filePdf))
				.body(new InputStreamResource(resource.getInputStream()));
    }
	
	public static ResponseEntity<String> createResponse(String message, HttpStatus httpStatus){
		return new ResponseEntity<>(String.format(RESULT_FORMAT, message), HeaderUtil.getJsonHeaders(), httpStatus);
    }
	
	private ResponseUtil() {
		// hide constructor
	}
	
}
