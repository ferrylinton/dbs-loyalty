package com.dbs.loyalty.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.util.UrlUtil;

import lombok.RequiredArgsConstructor;

/**
* A filter which logs web requests that lead to an error in the system.
*
*/
@RequiredArgsConstructor
@Component
public class LogRequestFilter extends OncePerRequestFilter implements Ordered {
	
	private static final String REGEX_JSON_PASSWORD = "(?i)(\\n?\\s*\".*password.*\"\\s*?:\\s*?\")[^\\n\"]*(\",?\\n?)";
	
	private static final String MASKED_PASSWORD = "$1*****$2";
	
    private static final String API = "/api"; 
    
    private static final String TADA = "/tada"; 
    
    private static final List<String> API_WITH_PASSWORD = Arrays.asList("/api/authenticate", "/api/customers/change-password", "/api/customers/activate");

	private static final int order = Ordered.LOWEST_PRECEDENCE - 8;

    private final LogAuditCustomerService logAuditCustomerService;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        
        filterChain.doFilter(requestWrapper, responseWrapper);
        
        logAuditCustomer(requestWrapper, responseWrapper);
        responseWrapper.copyBodyToResponse();
    }

    private void logAuditCustomer(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
    	if(!requestWrapper.getServletPath().contains(TADA) && requestWrapper.getServletPath().contains(API) && responseWrapper.getStatus() == 400) {
        	String url = UrlUtil.getFullUrl(requestWrapper);
        	String requestJson = getString(requestWrapper);
        	
        	if(API_WITH_PASSWORD.contains(requestWrapper.getServletPath())) {
        		requestJson = maskPassword(requestJson);
        	}
        	
        	logAuditCustomerService.saveBadRequest(url, requestWrapper.getServletPath(), requestJson, getString(responseWrapper));
        }
    }

    private String getString(ContentCachingRequestWrapper requestWrapper) {
    	if (requestWrapper != null) {
            return getString(requestWrapper.getContentAsByteArray(), requestWrapper.getCharacterEncoding());
        }else {
        	return null;
        }
    }
    
    private String getString(ContentCachingResponseWrapper responseWrapper) {
        if (responseWrapper != null) {
            return getString(responseWrapper.getContentAsByteArray(), responseWrapper.getCharacterEncoding());
        }else {
        	return null;
        }
    }
    
    private String getString(byte[] buf, String characterEncoding) {
        if (buf.length > 0) {
            try {
                return new String(buf, 0, buf.length, characterEncoding);
            }catch (UnsupportedEncodingException ex) {
                return ex.getMessage();
            }
        }else {
        	return null;
        }
    }
    
	private String maskPassword(String jsonString){
		if (jsonString == null) {
			return null;
		}

    	return jsonString.replaceAll(REGEX_JSON_PASSWORD, MASKED_PASSWORD);
	}

}
