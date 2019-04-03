package com.dbs.loyalty.security.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import com.dbs.loyalty.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class RestAccessFilter extends GenericFilterBean {

    private final RestTokenProvider restTokenProvider;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        String jwt = JwtUtil.resolveToken((HttpServletRequest) req);
        
        if (StringUtils.hasText(jwt) && restTokenProvider.validateToken(jwt)) {
            Authentication authentication = restTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(req, resp);
    }

}
