package com.dbs.loyalty.event;

import org.springframework.context.ApplicationEvent;

import com.dbs.loyalty.domain.LogLogin;

public class LoginEvent extends ApplicationEvent{

	private static final long serialVersionUID = 1L;
	
	private LogLogin logLogin;
	
	public LoginEvent(Object source, LogLogin logLogin) {
        super(source);
        this.logLogin = logLogin;
	}

	public LogLogin getLoginLog() {
		return logLogin;
	}

	public void setLoginLog(LogLogin logLogin) {
		this.logLogin = logLogin;
	}

}
