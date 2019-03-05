package com.dbs.loyalty.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="auditorProvider")
public class PersistenceConfiguration {

	private final String SYSTEM = "system";
	
	private final String ANONYMOUS_USER = "anonymousUser";
	
	@Bean
	public AuditorAware<String> auditorProvider() {
        return new AuditorAware<String>() {

        	@Override
        	public Optional<String> getCurrentAuditor() {
        		if(ANONYMOUS_USER.equals(SecurityContextHolder.getContext().getAuthentication().getName())){
        			return Optional.of(SYSTEM);
        		}else {
        			return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
        		}
        	}
		};
		
    }

}
