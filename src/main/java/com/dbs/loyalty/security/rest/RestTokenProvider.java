package com.dbs.loyalty.security.rest;

import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.ApplicationProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class RestTokenProvider {

	private Key key;
	
	private long tokenValidityInMilliseconds;

    private long tokenValidityInMillisecondsForRememberMe;
    
    private final ApplicationProperties applicationProperties;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode( applicationProperties.getSecurity().getSecret()));
        this.tokenValidityInMilliseconds = 1000 *  applicationProperties.getSecurity().getTokenValidityInSeconds();
        this.tokenValidityInMillisecondsForRememberMe = 1000 *  applicationProperties.getSecurity().getTokenValidityInSecondsForRememberMe();
    }
    
    public String createToken(RestAuthentication authentication, boolean rememberMe) {
    	long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts.builder()
            .setSubject(authentication.getCustomer().getId() + "," + authentication.getName())
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact();
    }

    public String createToken(String id, String email, String token) {
    	Jws<Claims> jws = parseClaimsJws(token);
    	
    	if(jws != null) {
    		return Jwts.builder()
                    .setSubject(id + "," + email)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setExpiration(jws.getBody().getExpiration())
                    .compact();
    	}else {
    		return null;
    	}
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();

        return new RestAuthentication(claims.getSubject().split(","));
    }

    public boolean validateToken(String token) {
    	Jws<Claims> jws = parseClaimsJws(token);
    	return jws != null;
    }
    
    private Jws<Claims> parseClaimsJws(String token) {
        try {
        	return Jwts.parser().setSigningKey(key).parseClaimsJws(token);	
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

        return null;
    }
    
}
