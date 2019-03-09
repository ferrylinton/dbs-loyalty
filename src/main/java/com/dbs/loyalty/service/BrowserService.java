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
import com.dbs.loyalty.domain.enumeration.LoginStatus;
import com.dbs.loyalty.util.IpUtil;

@Service
public class BrowserService {
		
	private final String USER_AGENT = "User-Agent";
	
	private final UserAgentParser parser;

	public BrowserService() throws IOException, ParseException {
		parser = new UserAgentService().loadParser();
	}

	public LogLogin createLogLogin(LoginStatus loginStatus, HttpServletRequest request) {
		Capabilities capabilities = parser.parse(request.getHeader(USER_AGENT));
		LogLogin logLogin = new LogLogin();
		logLogin.setIp(IpUtil.getInstance().getIp(request));
		logLogin.setCreatedDate(Instant.now());
		logLogin.setEmail(request.getParameter("email"));
		logLogin.setLoginStatus(loginStatus);
		logLogin.setBrowser(capabilities.getBrowser());
		logLogin.setBrowserType(capabilities.getBrowserType());
		logLogin.setBrowserMajorVersion(capabilities.getBrowserMajorVersion());
		logLogin.setDeviceType(capabilities.getDeviceType());
		logLogin.setPlatform(capabilities.getPlatform());
		logLogin.setPlatformVersion(capabilities.getPlatformVersion());

		return logLogin;
	}

}
