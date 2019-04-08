package com.dbs.loyalty.component;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.dbs.loyalty.util.MessageUtil;

@Component
public class ApplicationContextComponent implements ApplicationContextAware {
    
    @Override
    public void setApplicationContext(ApplicationContext context) {
    	MessageUtil.setMessageSource(context.getBean(MessageSource.class));
    }
    
}
