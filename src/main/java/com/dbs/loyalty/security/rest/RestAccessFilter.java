package com.dbs.loyalty.security.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.dbs.loyalty.util.JwtUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RestAccessFilter extends GenericFilterBean {

    private RestTokenProvider restTokenProvider;

    public RestAccessFilter(RestTokenProvider restTokenProvider) {
        this.restTokenProvider = restTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = JwtUtil.resolveToken(httpServletRequest);
        
        if (StringUtils.hasText(jwt) && restTokenProvider.validateToken(jwt)) {
            Authentication authentication = restTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
