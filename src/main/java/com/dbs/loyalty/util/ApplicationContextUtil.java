package com.dbs.loyalty.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.service.MessageService;

@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    
    @Override
    public void setApplicationContext(ApplicationContext context) {
    	MessageService.setMessageSource(context.getBean(MessageSource.class));
    }
    
}
