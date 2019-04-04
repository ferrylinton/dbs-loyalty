package com.dbs.loyalty.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.exception.LdapConnectException;

@Controller("loginController")
public class LoginController extends AbstractController{
	
	private String expired = "expired";
	
	private String invalid = "invalid";

	private String forbidden = "forbidden";
	
	private String logout = "logout";
	
	private String error = "error";
	
	private String badCredentials					= "message.invalidUsernameOrPassword";
	
	private String userIsAlreadyLogged				= "message.userIsAlreadyLogged";
	
	private String userIsLocked						= "message.userIsLocked";
	
	private String userLogoutSuccess				= "message.userLogoutSuccess";
	
	private String sessionIsExpired					= "message.sessionIsExpired";
	
	private String sessionIsInvalid					= "message.sessionIsInvalid";
	
	private String forbiddenMessage					= "message.forbidden";
	
	private String ldapConnectException				= "message.ldapConnectException";

	private String springSecurityLastException		= "textDanger";
	
	private String cssClass							= "class";
	
	private String textDanger 						= "text-danger";
	
	private String textSuccess						= "text-success";

	@GetMapping("/login")
	public String login(HttpServletRequest request) {
		
		if(request.getParameter(expired) != null) {
			request.setAttribute(cssClass, textDanger);
			request.setAttribute(Constant.MESSAGE, sessionIsExpired);
			
		}else if(request.getParameter(invalid) != null) {
			request.setAttribute(cssClass, textDanger);
			request.setAttribute(Constant.MESSAGE, sessionIsInvalid);
			
		}else if(request.getParameter(forbidden) != null) {
			request.setAttribute(cssClass, textDanger);
			request.setAttribute(Constant.MESSAGE, forbiddenMessage);
			
		}else if(request.getParameter(logout) != null) {
			request.setAttribute(cssClass, textSuccess);
			request.setAttribute(Constant.MESSAGE, userLogoutSuccess);
			
		}else if (request.getParameter(error) != null) {
			Exception exception = (Exception) request.getSession().getAttribute(springSecurityLastException);
			
			request.setAttribute(cssClass, textDanger);
			if(exception instanceof LockedException) {
				request.setAttribute(Constant.MESSAGE, userIsLocked);
			}else if(exception instanceof SessionAuthenticationException) {
				request.setAttribute(Constant.MESSAGE, userIsAlreadyLogged);
			}else if(exception instanceof LdapConnectException) {
				request.setAttribute(Constant.MESSAGE, ldapConnectException);
			}else{
				request.setAttribute(Constant.MESSAGE, badCredentials);
			}
		}
		
		return "login/form";
	}

}
