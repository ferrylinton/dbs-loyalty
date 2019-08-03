package com.dbs.loyalty.util;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.FilePdf;

public final class ResponseUtil {

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
	
	private ResponseUtil() {
		// hide constructor
	}
	
}
