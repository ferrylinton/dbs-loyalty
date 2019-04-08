package com.dbs.loyalty.web.controller;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dbs.loyalty.config.constant.Constant;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.service.dto.UserDto;
import com.dbs.loyalty.service.dto.UserPasswordDto;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.UrlUtil;
import com.dbs.loyalty.web.response.AbstractResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserPasswordController extends AbstractController{

	private String formTemplate	= "password/form";
	
	private String successMessage = "message.changePasswordSuccess";
	
	private String user = "user";
	
	private String userType = "userType";
	
	private String pass = "password";
	
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/password")
	public String viewPassword(ModelMap model, Principal principal){
		Optional<UserDto> userDto = userService.findByUsername(principal.getName());
		
		if(userDto.isPresent()) {
			model.addAttribute(userType, userDto.get().getUserType());
		}
		
		UserPasswordDto userPasswordDto = new UserPasswordDto();
		userPasswordDto.setLoggedUsername(principal.getName());
		userPasswordDto.setUsername(principal.getName());

		model.addAttribute(pass, userPasswordDto);
		model.addAttribute(Constant.ENTITY_URL, UrlUtil.getUrl(pass));
		return formTemplate;
	}
	
	@PreAuthorize("hasRole('USER_MK')")
	@GetMapping("/password/{username}")
	public String viewPassword(ModelMap model, @PathVariable String username, Principal principal) throws NotFoundException {
		Optional<UserDto> userDto = userService.findByUsername(username);
		
		if(userDto.isPresent()) {
			UserPasswordDto userPasswordDto = new UserPasswordDto();
			userPasswordDto.setLoggedUsername(principal.getName());
			userPasswordDto.setUsername(principal.getName());
			
			model.addAttribute(userType, userDto.get().getUserType());
			model.addAttribute(pass, userPasswordDto);
			model.addAttribute(Constant.ENTITY_URL, UrlUtil.getUrl(pass));
		}else {
			throw new NotFoundException();
		}

		return formTemplate;
	}

	@PreAuthorize("authenticated")
	@PostMapping("/password")
	@ResponseBody
	public ResponseEntity<AbstractResponse> save(@Valid @ModelAttribute UserPasswordDto userPasswordDto, BindingResult result) throws BadRequestException {
		if (result.hasErrors()) {
			throwBadRequestResponse(result);
		} 

		String passwordHash = PasswordUtil.encode(userPasswordDto.getPasswordPlain());
		userService.save(userPasswordDto.getUsername(), passwordHash);

		String message = MessageUtil.getMessage(successMessage, userPasswordDto.getUsername());
		String resultUrl = UrlUtil.getUrl(userPasswordDto.isOwnPassword() ? pass : user);
		return dataIsSavedResponse(message, resultUrl);
	}
	
}
