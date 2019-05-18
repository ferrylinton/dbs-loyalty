package com.dbs.loyalty.security.rest;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.config.ApplicationProperties;
import com.dbs.loyalty.model.TokenData;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private final ApplicationProperties applicationProperties;
    
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(applicationProperties.getSecurity().getSecret()));
    }
 
    public String createToken(TokenData tokenData, Date validity) throws IOException {
    	return Jwts.builder()
                .setSubject(objectMapper.writeValueAsString(tokenData))
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(validity)
                .compact();
    }
    
    public Authentication getAuthentication(String token) throws IOException {
        Claims claims = Jwts.parser()
            .setSigningKey(key)
            .parseClaimsJws(token)
            .getBody();

        TokenData tokenData = objectMapper.readValue(claims.getSubject(), TokenData.class);
        return new RestAuthentication(tokenData);
    }

    public boolean validateToken(String token) {
    	Jws<Claims> jws = parseClaimsJws(token);
    	return jws != null;
    }
    
    public Date getExpiration(String token) {
    	Jws<Claims> jws = parseClaimsJws(token);
    	return jws.getBody().getExpiration();
    }
    
    private Jws<Claims> parseClaimsJws(String token) {
        try {
        	return Jwts.parser().setSigningKey(key).parseClaimsJws(token);	
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error(e.getLocalizedMessage());
        } catch (ExpiredJwtException e) {
        	log.error(e.getLocalizedMessage());
        } catch (UnsupportedJwtException e) {
        	log.error(e.getLocalizedMessage());
        } catch (IllegalArgumentException e) {
        	log.error(e.getLocalizedMessage());
        }

        return null;
    }
    
}
