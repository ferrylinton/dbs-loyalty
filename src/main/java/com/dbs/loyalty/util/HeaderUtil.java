package com.dbs.loyalty.util;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.FilePdf;

public final class HeaderUtil {
	
	private static final String CONTENT_DISPOSITION = "content-disposition";

	private static final String CONTENT_DISPOSITION_FORMAT = "attachment;filename=%s.pdf";
	
	private static HttpHeaders jsonHeaders;

	public static HttpHeaders geImageHeaders(FileImage fileImage) {
    	HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		headers.setContentType(MediaType.valueOf(fileImage.getContentType()));
		return headers;
    }
	
	public static HttpHeaders gePdfHeaders(FilePdf filePdf) {
		HttpHeaders headers = new HttpHeaders();
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.add(CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_FORMAT, filePdf.getId()));
		return headers;
    }
    
	public static HttpHeaders getJsonHeaders() {
		if(jsonHeaders == null) {
			jsonHeaders = new HttpHeaders();
			jsonHeaders.setContentType(MediaType.APPLICATION_JSON);
		}
		
		return jsonHeaders;
	}	
	
	private HeaderUtil() {
		// hide constructor
	}
	
}
