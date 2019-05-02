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
import com.dbs.loyalty.util.HeaderTokenUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestAccessFilter extends GenericFilterBean {

	private final String JWT_TOKEN_NOT_FOUND = "JWT Token not found";
	
	private final String EXPIRED_JWT_TOKEN = "Expired JWT token";
	
    private final RestTokenProvider restTokenProvider;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        String jwt = HeaderTokenUtil.resolveToken((HttpServletRequest) req);
        
        if(jwt == null){
        	req.setAttribute(Constant.MESSAGE, JWT_TOKEN_NOT_FOUND);
        }else if(restTokenProvider.validateToken(jwt)) {
            Authentication authentication = restTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else {
        	req.setAttribute(Constant.MESSAGE, EXPIRED_JWT_TOKEN);
        }
        
        filterChain.doFilter(req, resp);
    }

}
