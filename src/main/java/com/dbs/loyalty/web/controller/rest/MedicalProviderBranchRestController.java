package com.dbs.loyalty.web.controller.rest;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.MedicalProviderBranch;
import com.dbs.loyalty.service.MedicalProviderBranchService;
import com.dbs.loyalty.service.dto.MedicalProviderBranchDto;
import com.dbs.loyalty.service.dto.MedicalProviderDto;
import com.dbs.loyalty.service.mapper.MedicalProviderBranchMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Medical Provider Branch.
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.WELLNESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/medical-provider-branches")
public class MedicalProviderBranchRestController {

	public static final String GET_MEDICAL_PROVIDER_BRANCHES = "GetMedicalProviderBranches";
	
    private final MedicalProviderBranchService medicalProviderBranchService;
    
    private final MedicalProviderBranchMapper medicalProviderBranchMapper;
    
    /**
     * GET  /api/medical-provider-branches : get all medical provider branches
     *
     * @return the list of medical provider branches
     */
    @ApiOperation(
    		nickname=GET_MEDICAL_PROVIDER_BRANCHES, 
    		value=GET_MEDICAL_PROVIDER_BRANCHES, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=MedicalProviderDto.class)})
    @EnableLogAuditCustomer(operation=GET_MEDICAL_PROVIDER_BRANCHES)
    @GetMapping("/{medicalProviderCityId}")
    public List<MedicalProviderBranchDto> getMedicalProviderBranches(
    		@ApiParam(name = "MedicalProviderCityId", value = "MedicalProviderCityId", example = "6nJfmxAD6GWtsehXfSkShg")
    		@PathVariable String medicalProviderCityId) {
    	
    	List<MedicalProviderBranch> medicalProviderBranches = medicalProviderBranchService.findByMedicalProviderCityId(medicalProviderCityId);
    	return medicalProviderBranchMapper.toDto(medicalProviderBranches);
    }

}
