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
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.MessageService;
import com.dbs.loyalty.service.UserService;
import com.dbs.loyalty.service.dto.UserDto;
import com.dbs.loyalty.service.dto.UserPasswordDto;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.ResponseUtil;
import com.dbs.loyalty.util.UrlUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserPasswordController extends AbstractController{

	private final String FORM_TEMPLATE	= "password/form";
	
	private final String PWD_MESSAGE = "message.password";
	
	private final String USER = "user";
	
	private final String PWD = "password";
	
	private final UserService userService;

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/password")
	public String viewPassword(ModelMap model, Principal principal) throws NotFoundException {
		Optional<UserDto> userDto = userService.findByUsername(principal.getName());
		UserPasswordDto userPasswordDto = new UserPasswordDto();
		userPasswordDto.setLoggedUsername(principal.getName());
		userPasswordDto.setUsername(principal.getName());

		model.addAttribute("userType", userDto.get().getUserType());
		model.addAttribute(PWD, userPasswordDto);
		model.addAttribute(Constant.ENTITY_URL, UrlUtil.getUrl(PWD));
		return FORM_TEMPLATE;
	}
	
	@PreAuthorize("hasRole('USER_MK')")
	@GetMapping("/password/{username}")
	public String viewPassword(ModelMap model, @PathVariable String username, Principal principal) throws NotFoundException {
		Optional<UserDto> userDto = userService.findByUsername(username);
		
		if(userDto.isPresent()) {
			UserPasswordDto userPasswordDto = new UserPasswordDto();
			userPasswordDto.setLoggedUsername(principal.getName());
			userPasswordDto.setUsername(principal.getName());
			
			model.addAttribute("userType", userDto.get().getUserType());
			model.addAttribute(PWD, userPasswordDto);
			model.addAttribute(Constant.ENTITY_URL, UrlUtil.getUrl(PWD));
		}else {
			throw new NotFoundException();
		}

		return FORM_TEMPLATE;
	}

	@PreAuthorize("authenticated")
	@PostMapping("/password")
	@ResponseBody
	public ResponseEntity<?> save(@Valid @ModelAttribute(PWD) UserPasswordDto userPasswordDto, BindingResult result) throws NotFoundException {
		if (result.hasErrors()) {
			return ResponseUtil.createBadRequestResponse(result);
		} else {
			try {
				String passwordHash = PasswordUtil.getInstance().encode(userPasswordDto.getPasswordPlain());
				userService.save(userPasswordDto.getUsername(), passwordHash);

				String message = MessageService.getMessage(PWD_MESSAGE, userPasswordDto.getUsername());
				String resultUrl = UrlUtil.getUrl(userPasswordDto.isOwnPassword() ? PWD : USER);
				return ResponseUtil.createSuccessResponse(message, resultUrl);
			} catch (Exception ex) {
				log.error(ex.getLocalizedMessage(), ex);
				return errorResponse(ex);
			}
		}
	}
	
}
