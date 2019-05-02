package com.dbs.loyalty.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class HeaderTokenUtil {

	public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String AUTHORIZATION_TOKEN = "access_token";
    
    public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    
    public static String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHORIZATION_TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        
        String jwt = request.getParameter(AUTHORIZATION_TOKEN);
        
        if (StringUtils.hasText(jwt)) {
            return jwt;
        }
        
        return null;
    }
    
    private HeaderTokenUtil() {
    	// hide constructor
    }
	
}
