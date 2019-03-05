package com.dbs.loyalty.service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.dbs.loyalty.config.Constant;
import com.dbs.loyalty.domain.Approval;
import com.dbs.loyalty.domain.Role;
import com.dbs.loyalty.domain.enumeration.ApprovalType;
import com.dbs.loyalty.domain.enumeration.DataType;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.model.ErrorResponse;
import com.dbs.loyalty.repository.ApprovalRepository;
import com.dbs.loyalty.repository.RoleRepository;
import com.dbs.loyalty.util.ResponseUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApprovalService {

	private final String ENTITY_NAME = "approval";
	
	private final ApprovalRepository approvalRepository;
	
	private final RoleRepository roleRepository;
	
	private final ObjectMapper objectMapper;
	
	public ApprovalService(ApprovalRepository approvalRepository, RoleRepository roleRepository, ObjectMapper objectMapper) {
		this.approvalRepository = approvalRepository;
		this.roleRepository = roleRepository;
		this.objectMapper = objectMapper;
	}
	
	public Optional<Approval> findById(String id) throws NotFoundException {
		Optional<Approval> approval = approvalRepository.findById(id);

		if (approval.isPresent()) {
			return approval;
		} else {
			throw new NotFoundException();
		}
	}
	
	public Page<Approval> findAll(Pageable pageable){
		return approvalRepository.findAll(pageable);
	}
	
	public Approval save(DataType dataType, Object value) throws JsonProcessingException {
		Approval approval = new Approval();
		approval.setApprovalType(ApprovalType.Pending);
		approval.setDataType(dataType);
		approval.setDataJson(objectMapper.writeValueAsString(value));
		approval.setCreatedBy(SecurityUtil.getCurrentEmail());
		approval.setCreatedDate(Instant.now());
		return approvalRepository.save(approval);
	}
	
	public ResponseEntity<?> save(Approval approval) {
		try {
			System.out.println(approval.getId());
			System.out.println(approval.getApproved());
			System.out.println(approval.getMessage());
			System.out.println(approval.getDataJson());
			System.out.println(approval.getApprovalType());
			System.out.println(approval.getDataType());
			
			if(approval.getDataType() == DataType.Role) {
				roleRepository.save(objectMapper.readValue(approval.getDataJson(), Role.class));
			}
			return ResponseUtil.createSaveResponse(approval.getId(), ENTITY_NAME);
		} catch (Exception ex) {
			return ResponseEntity
		            .status(HttpStatus.INTERNAL_SERVER_ERROR)
		            .body(new ErrorResponse(ex.getLocalizedMessage()));
		}
	}
	
	public void viewForm(ModelMap model, String id) throws NotFoundException {
		if (id.equals(Constant.ZERO)) {
			model.addAttribute(ENTITY_NAME, new Approval());
		} else {
			Optional<Approval> approval = findById(id);
			model.addAttribute(ENTITY_NAME, approval.get());
		}
	}
}
