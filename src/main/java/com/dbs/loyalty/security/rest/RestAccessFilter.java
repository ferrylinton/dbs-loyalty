package com.dbs.loyalty.security.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.util.HeaderTokenUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestAccessFilter extends GenericFilterBean {
	
	public static final String NOT_FOUND_TOKEN = "Token is not found";
	
    private final RestTokenProvider restTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = HeaderTokenUtil.resolveToken((HttpServletRequest) request);
        
        if(token == null){
        	request.setAttribute(Constant.MESSAGE, NOT_FOUND_TOKEN);
        }else if(restTokenProvider.validateToken(token)) {
            Authentication authentication = restTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
        	request.setAttribute(Constant.MESSAGE, MessageConstant.LOGIN_FAILED);
        }
        
        filterChain.doFilter(request, response);
    }

}
