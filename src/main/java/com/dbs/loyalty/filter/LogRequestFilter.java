package com.dbs.loyalty.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import com.dbs.loyalty.domain.LogApi;
import com.dbs.loyalty.service.LogApiService;
import com.dbs.loyalty.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* A filter which logs web requests that lead to an error in the system.
*
*/
@Slf4j
@RequiredArgsConstructor
@Component
public class LogRequestFilter extends OncePerRequestFilter implements Ordered {
	
    private final static int order = Ordered.LOWEST_PRECEDENCE - 8;
    
    private final static String API_AUTHENTICATE = "/api/authenticate";
    
    private final static String STATIC = "/static";
    
    private final static String API = "/api";
    
    private final static String EXCEPTION_ATTRIBUTE = "javax.servlet.error.exception";
    
    private final LogApiService logApiService;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);
        
        if(request.getRequestURI().contains(API) && !request.getRequestURI().contains(API_AUTHENTICATE) && !request.getRequestURI().contains(STATIC)) {
        	LogApi logApi = getLogAudit(request, response);
            logApi.setBody(getBody(wrappedRequest));
            logApi.setError(getError(request));
            logApi.setCreatedBy(SecurityUtil.getLogged());
            logApi.setCreatedDate(Instant.now());
            logApiService.save(logApi);
            log.info(logApi.toString());
        }
    }

    private LogApi getLogAudit(HttpServletRequest request, HttpServletResponse response) {
    	LogApi logApi = new LogApi();
    	logApi.setRequestURI(request.getRequestURI());
    	logApi.setQueryString(request.getQueryString());
    	logApi.setMethod(request.getMethod());
    	logApi.setStatus(response.getStatus());
    	
    	return logApi;
    }
    
    private String getBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                }catch (UnsupportedEncodingException ex) {
                    return ex.getMessage();
                }
            }
        }
        
        return null;
    }

    private String getError(HttpServletRequest request) {
    	if(request.getAttribute(EXCEPTION_ATTRIBUTE) instanceof Throwable) {
    		Throwable exception = (Throwable) request.getAttribute(EXCEPTION_ATTRIBUTE);
            
            if(exception != null) {
            	return exception.getMessage();
            }
    	}
    	
    	return null;
    }

}
