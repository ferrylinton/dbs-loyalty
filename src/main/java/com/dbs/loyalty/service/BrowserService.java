package com.dbs.loyalty.service;

import java.io.IOException;
import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;
import com.dbs.loyalty.domain.LogLogin;
import com.dbs.loyalty.util.IpUtil;

@Service
public class BrowserService {
		
	private String userAgent = "User-Agent";
	
	private String username = "username";
	
	private final UserAgentParser parser;

	public BrowserService() throws IOException, ParseException {
		parser = new UserAgentService().loadParser();
	}

	public LogLogin createLogLogin(String status, HttpServletRequest request) {
		Capabilities capabilities = parser.parse(request.getHeader(userAgent));
		LogLogin logLogin = new LogLogin();
		logLogin.setIp(IpUtil.getIp(request));
		logLogin.setCreatedDate(Instant.now());
		logLogin.setUsername(request.getParameter(username));
		logLogin.setStatus(status);
		logLogin.setBrowser(capabilities.getBrowser());
		logLogin.setBrowserType(capabilities.getBrowserType());
		logLogin.setBrowserMajorVersion(capabilities.getBrowserMajorVersion());
		logLogin.setDeviceType(capabilities.getDeviceType());
		logLogin.setPlatform(capabilities.getPlatform());
		logLogin.setPlatformVersion(capabilities.getPlatformVersion());

		return logLogin;
	}

}
