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

import com.dbs.loyalty.domain.LogAudit;
import com.dbs.loyalty.service.LogAuditService;
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
	
    // put filter at the end of all other filters to make sure we are processing after all others
    private final static int order = Ordered.LOWEST_PRECEDENCE - 8;
    
    private final static String API_AUTHENTICATE = "/api/authenticate";
    
    private final LogAuditService logAuditService;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);

        // pass through the actual request handling
        filterChain.doFilter(wrappedRequest, response);
        
        if(!request.getRequestURI().contains(API_AUTHENTICATE)) {
        	LogAudit logAudit = getLogAudit(request, response);
            logAudit.setBody(getBody(wrappedRequest));
            logAudit.setError(getError(request));
            logAudit.setCreatedBy(SecurityUtil.getId());
            logAudit.setCreatedDate(Instant.now());
            logAuditService.save(logAudit);
            log.info(logAudit.toString());
        }
        
    }

    private LogAudit getLogAudit(HttpServletRequest request, HttpServletResponse response) {
    	LogAudit logAudit = new LogAudit();
    	logAudit.setRequestURI(request.getRequestURI());
    	logAudit.setQueryString(request.getQueryString());
    	logAudit.setMethod(request.getMethod());
    	logAudit.setStatus(response.getStatus());
    	
    	return logAudit;
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
        Throwable exception = (Throwable) request.getAttribute("javax.servlet.error.exception");
        
        if(exception != null) {
        	return exception.getMessage();
        }else {
        	return null;
        }
    }

}
