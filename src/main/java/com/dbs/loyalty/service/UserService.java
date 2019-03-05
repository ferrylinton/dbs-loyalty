package com.dbs.loyalty.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.User;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.repository.UserRepository;
import com.dbs.loyalty.util.PasswordUtil;
import com.dbs.loyalty.util.ResponseUtil;
import com.dbs.loyalty.util.UrlUtil;


@Service
public class UserService{
	
	private final Logger LOG = LoggerFactory.getLogger(UserService.class);

	private final String ENTITY_NAME = "user";

	private final UserRepository userRepository;
 
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public Optional<User> findByEmail(String email){
		return userRepository.findByEmail(email);
	}
	
	public Optional<User> findById(String id) throws NotFoundException {
		Optional<User> user = userRepository.findById(id);
		
		if(user.isPresent()) {
			return user;
		}else {
			throw new NotFoundException();
		}
	}
	
	public Page<User> findAll(String keyword, Pageable pageable) {
		if(keyword.equals(Constant.EMPTY)) {
			return userRepository.findAll(pageable);
		}else {
			return userRepository.findAllByEmailContainingAllIgnoreCase(keyword, pageable);
		}
	}
	
	public boolean isEmailExist(User user) {
		return isExist(userRepository.findByEmailIgnoreCase(user.getEmail()), user.getId());
	}
	
	private boolean isExist(Optional<User> user, String id) {
		if(user.isPresent()) {
			if(id == null) {
				return true;
			}else if(!id.equals(user.get().getId())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void viewForm(ModelMap model, String id) throws NotFoundException {
		if(id.equals(Constant.ZERO)) {
			model.addAttribute(ENTITY_NAME, new User());
		}else {
			Optional<User> user = findById(id);
			model.addAttribute(ENTITY_NAME, user.get());
		}
	}
	
	public ResponseEntity<?> save(User user) {
		try {
			if(user.getId() == null) {
				user.setPasswordHash(PasswordUtil.getInstance().encode(user.getPasswordPlain()));
			}else {
				Optional<User> current = userRepository.findById(user.getId());
				user.setPasswordHash(current.get().getPasswordHash());
				user.setImageBytes(current.get().getImageBytes());
			}
			
			userRepository.save(user);
			return ResponseUtil.createSaveResponse(user.getEmail(), ENTITY_NAME);
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}
	
	public ResponseEntity<?> delete(String id) throws NotFoundException {
		try {
			Optional<User> user = userRepository.findById(id);
			userRepository.delete(user.get());
			return ResponseUtil.createDeleteResponse(user.get().getEmail(), ENTITY_NAME);
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}
 
	public String getEntityUrl() {
		return UrlUtil.getEntityUrl(ENTITY_NAME);
	}
	
}
