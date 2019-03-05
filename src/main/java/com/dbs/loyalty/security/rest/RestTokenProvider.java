package com.dbs.loyalty.security.rest;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.dbs.loyalty.config.SecurityProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class RestTokenProvider {
	
	private final Logger log = LoggerFactory.getLogger(RestTokenProvider.class);

	private Key key;
	
	private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;
    
    private final SecurityProperties securityProperties;
    
    public RestTokenProvider(SecurityProperties securityProperties) {
    	this.securityProperties = securityProperties;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        String secret = securityProperties.getSecret();
        
        if (!StringUtils.isEmpty(secret)) {
            log.warn("Warning: the JWT key used is not Base64-encoded.");
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        } else {
            keyBytes = Decoders.BASE64.decode(securityProperties.getBase64Secret());
        }
        
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = 1000 * securityProperties.getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe = 1000 * securityProperties.getTokenValidityInSecondsForRememberMe();
    }
    
    public String createToken(Authentication authentication, boolean rememberMe) {
    	long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getName())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();

        return new RestAuthentication(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: {}", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: {}", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: {}", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: {}", e);
        }
        return false;
    }
    
}
