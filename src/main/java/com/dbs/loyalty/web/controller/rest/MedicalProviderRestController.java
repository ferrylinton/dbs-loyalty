package com.dbs.loyalty.web.controller.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.MedicalProvider;
import com.dbs.loyalty.service.MedicalProviderService;
import com.dbs.loyalty.service.dto.MedicalProviderDto;
import com.dbs.loyalty.service.mapper.MedicalProviderMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Medical Provider.
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.WELLNESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/medical-providers")
public class MedicalProviderRestController {
	
	public static final String GET_MEDICAL_PROVIDERS = "GetMedicalProviders";

    private final MedicalProviderService medicalProviderService;
    
    private final MedicalProviderMapper medicalProviderMapper;
    
    /**
     * GET  /api/medical-providers : get all medical providers
     *
     * @return the list of medical providers
     */
    @ApiOperation(
    		nickname=GET_MEDICAL_PROVIDERS, 
    		value=GET_MEDICAL_PROVIDERS, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=MedicalProviderDto.class)})
    @EnableLogAuditCustomer(operation=GET_MEDICAL_PROVIDERS)
    @GetMapping
    public List<MedicalProviderDto> getMedicalProviders(HttpServletRequest request, HttpServletResponse response) {
    	List<MedicalProvider> medicalProviders = medicalProviderService.findAll();
    	return medicalProviderMapper.toDto(medicalProviders);
    }

}
