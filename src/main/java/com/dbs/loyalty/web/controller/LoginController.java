package com.dbs.loyalty.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dbs.loyalty.config.Constant;

@Controller("loginController")
public class LoginController extends AbstractController{
	
	private final String EXPIRED = "expired";
	
	private final String INVALID = "invalid";

	private final String FORBIDDEN = "forbidden";
	
	private final String LOGOUT = "logout";
	
	private final String ERROR = "error";
	
	private final String INVALID_USERNAME_OR_PASSWORD		= "message.invalidUsernameOrPassword";
	
	private final String USER_IS_ALREADY_LOGGED			= "message.userIsAlreadyLogged";
	
	private final String USER_IS_LOCKED					= "message.userIsLocked";
	
	private final String USER_LOGOUT_SUCCESS			= "message.userLogoutSuccess";
	
	private final String SESSION_IS_EXPIRED				= "message.sessionIsExpired";
	
	private final String SESSION_IS_INVALID				= "message.sessionIsInvalid";
	
	private final String FORBIDDEN_MESSAGE				= "message.forbidden";

	private final String SPRING_SECURITY_LAST_EXCEPTION	= "SPRING_SECURITY_LAST_EXCEPTION";
	
	private final String CLASS							= "class";
	
	private final String TEXT_DANGER 					= "text-danger";
	
	private final String TEXT_SUCCESS					= "text-success";
	
	private final String LOGIN_TEMPLATE					= "login/form";
	

	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		
		if(request.getParameter(EXPIRED) != null) {
			request.setAttribute(CLASS, TEXT_DANGER);
			request.setAttribute(Constant.MESSAGE, SESSION_IS_EXPIRED);
			
		}else if(request.getParameter(INVALID) != null) {
			request.setAttribute(CLASS, TEXT_DANGER);
			request.setAttribute(Constant.MESSAGE, SESSION_IS_INVALID);
			
		}else if(request.getParameter(FORBIDDEN) != null) {
			request.setAttribute(CLASS, TEXT_DANGER);
			request.setAttribute(Constant.MESSAGE, FORBIDDEN_MESSAGE);
			
		}else if(request.getParameter(LOGOUT) != null) {
			request.setAttribute(CLASS, TEXT_SUCCESS);
			request.setAttribute(Constant.MESSAGE, USER_LOGOUT_SUCCESS);
			
		}else if (request.getParameter(ERROR) != null) {
			Exception exception = (Exception) request.getSession().getAttribute(SPRING_SECURITY_LAST_EXCEPTION);
			
			request.setAttribute(CLASS, TEXT_DANGER);
			if(exception instanceof LockedException) {
				request.setAttribute(Constant.MESSAGE, USER_IS_LOCKED);
			}else if(exception instanceof SessionAuthenticationException) {
				request.setAttribute(Constant.MESSAGE, USER_IS_ALREADY_LOGGED);
			}else{
				request.setAttribute(Constant.MESSAGE, INVALID_USERNAME_OR_PASSWORD);
			}
		}
		
		return LOGIN_TEMPLATE;
	}

}
