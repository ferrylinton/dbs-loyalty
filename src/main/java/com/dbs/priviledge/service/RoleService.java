package com.dbs.priviledge.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dbs.priviledge.config.Constant;
import com.dbs.priviledge.domain.Role;
import com.dbs.priviledge.exception.NotFoundException;
import com.dbs.priviledge.model.ErrorResponse;
import com.dbs.priviledge.repository.RoleRepository;
import com.dbs.priviledge.util.ResponseUtil;
import com.dbs.priviledge.util.UrlUtil;

@Service
public class RoleService{

	private final Logger LOG = LoggerFactory.getLogger(RoleService.class);
	
	private final String ENTITY_NAME = "role";

	private final RoleRepository roleRepository;
	
	public RoleService(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public Optional<Role> findById(String id) throws NotFoundException {
		Optional<Role> role = roleRepository.findById(id);

		if (role.isPresent()) {
			return role;
		} else {
			throw new NotFoundException();
		}
	}

	public Page<Role> findAll(String keyword, Pageable pageable) {
		if (keyword == null || keyword.equals(Constant.EMPTY)) {
			return roleRepository.findAll(pageable);
		} else {
			return roleRepository.findAllByNameContainingAllIgnoreCase(keyword.trim(), pageable);
		}
	}
	
	public List<Role> findAll(){
		return roleRepository.findAll();
	}

	public Optional<Role> findByName(String name) {
		return roleRepository.findByNameIgnoreCase(name);
	}

	public boolean isNameExist(Role role) {
		Optional<Role> obj = roleRepository.findByNameIgnoreCase(role.getName());

		if (obj.isPresent()) {
			if (role.getId() == null) {
				return true;
			} else if (!role.getId().equals(obj.get().getId())) {
				return true;
			}
		}

		return false;
	}
	
	public void viewForm(ModelMap model, String id) throws NotFoundException {
		if (id.equals(Constant.ZERO)) {
			model.addAttribute(ENTITY_NAME, new Role());
		} else {
			Optional<Role> role = findById(id);
			model.addAttribute(ENTITY_NAME, role.get());
		}
	}

	public ResponseEntity<?> save(Role role) {
		try {
			role = roleRepository.save(role);
			return ResponseUtil.createSaveResponse(role.getName(), ENTITY_NAME);
		} catch (Exception ex) {
			LOG.error(ex.getLocalizedMessage(), ex);
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}

	public ResponseEntity<?> delete(String id) throws NotFoundException {
		try {
			Optional<Role> role = findById(id);
			roleRepository.delete(role.get());
			return ResponseUtil.createDeleteResponse(role.get().getName(), ENTITY_NAME);
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
