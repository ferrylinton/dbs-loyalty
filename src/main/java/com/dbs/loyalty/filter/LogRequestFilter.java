package com.dbs.loyalty.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
	
    private final static int order = Ordered.LOWEST_PRECEDENCE - 8;
    
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
        
        if(response.getStatus() == 400) {
        	logAuditCustomerService.saveError(UrlUtil.getFullUrl(request), request.getServletPath(), getString(requestWrapper), getString(responseWrapper));
        }
        
        responseWrapper.copyBodyToResponse();
    }

    private String getString(ContentCachingRequestWrapper requestWrapper) {
        if (requestWrapper != null) {
            byte[] buf = requestWrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    String result = new String(buf, 0, buf.length, requestWrapper.getCharacterEncoding());
                    return result.replace("\n", "").replace("\r", "");
                }catch (UnsupportedEncodingException ex) {
                    return ex.getMessage();
                }
            }
        }
        
        return null;
    }

    private String getString(ContentCachingResponseWrapper responseWrapper) {
        if (responseWrapper != null) {
            byte[] buf = responseWrapper.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    String result = new String(buf, 0, buf.length, responseWrapper.getCharacterEncoding());
                    return result.replace("\n", "").replace("\r", "");
                }catch (UnsupportedEncodingException ex) {
                    return ex.getMessage();
                }
            }
        }
        
        return null;
    }

}
