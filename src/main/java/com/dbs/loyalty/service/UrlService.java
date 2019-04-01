package com.dbs.loyalty.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UrlService {

	private final HttpServletRequest request;

	public String getUrl(String resource, String type, String id) {
		String scheme = request.getScheme();
	    String serverName = request.getServerName();
	    int serverPort = request.getServerPort();
	    String contextPath = request.getContextPath();
	   
	    StringBuilder url = new StringBuilder();
	    url.append(scheme).append("://").append(serverName);

	    if (serverPort != 80 && serverPort != 443) {
	        url.append(":").append(serverPort);
	    }

	    url.append(contextPath).append("/").append(resource);
	    
	    if(id != null) {
	    	 url.append("/").append(id);
	    }
	    
	    url.append("/").append(type);
	    return url.toString();
	}
	
}
