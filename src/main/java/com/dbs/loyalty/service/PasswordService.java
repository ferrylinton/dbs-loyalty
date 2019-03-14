package com.dbs.loyalty.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.model.Password;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.ResponseUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;


@Service
public class PasswordService{
	
	private final Logger LOG = LoggerFactory.getLogger(PasswordService.class);

	private final String USER = "user";
	
	private final String ENTITY_NAME = "password";
	
	private final String PASSWORD_MESSAGE = "message.password";

	private final UserRepository userRepository;

	public PasswordService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void viewPassword(ModelMap model, String email) throws NotFoundException {
		Password password = new Password();
		password.setLoggedEmail(SecurityUtil.getCurrentEmail());
		password.setEmail(email);

		model.addAttribute(ENTITY_NAME, password);
		model.addAttribute(Constant.ENTITY_URL, UrlUtil.getEntityUrl(ENTITY_NAME));
	}
	
	public ResponseEntity<?> save(Password password) throws NotFoundException {
		try {
			Optional<User> user = userRepository.findByUsernameIgnoreCase(password.getEmail());
			
			if(user.isPresent()) {
				user.get().setPasswordPlain(password.getPasswordPlain());
				user.get().setPasswordHash(PasswordUtil.getInstance().encode(password.getPasswordPlain()));
				userRepository.save(user.get());
			}else {
				throw new NotFoundException();
			}

			String message = MessageService.getMessage(PASSWORD_MESSAGE, password.getEmail());
			String resultUrl = UrlUtil.getEntityUrl(password.isOwnPassword() ? ENTITY_NAME : USER);
			return ResponseUtil.createSuccessResponse(message, resultUrl);
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}

}
