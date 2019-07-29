package com.dbs.loyalty.util;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.dbs.loyalty.domain.FilePdf;

public final class PdfUtil {
	
	private static final String CONTENT_DISPOSITION = "content-disposition";

	private static final String CONTENT_DISPOSITION_FORMAT = "attachment;filename=%s.pdf";
	
	public static HttpHeaders geHeaders(FilePdf filePdf) {
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.add(CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT, filePdf.getId()));
		return headers;
    }
    
	public static ResponseEntity<InputStreamResource> getResponse(FilePdf filePdf) throws IOException{
		ByteArrayResource resource = new ByteArrayResource(filePdf.getBytes());
		
		return ResponseEntity
				.ok()
				.headers(geHeaders(filePdf))
				.body(new InputStreamResource(resource.getInputStream()));
    }
	
	private PdfUtil() {
		// hide constructor
	}
	
}
