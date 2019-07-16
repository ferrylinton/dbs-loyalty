package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.GET_MEDICAL_PROVIDERS;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.WELLNESS;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.med.MedicalProvider;
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
@Api(tags = { WELLNESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
public class MedicalProviderRestController {

    private final MedicalProviderService medicalProviderService;
    
    private final MedicalProviderMapper medicalProviderMapper;
    
    /**
     * GET  /api/medical-providers : get all medical providers
     *
     * @return the list of medical providers
     */
    @ApiOperation(nickname=GET_MEDICAL_PROVIDERS, value=GET_MEDICAL_PROVIDERS, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response=MedicalProviderDto.class)})
    @GetMapping("/api/medical-providers")
    public List<MedicalProviderDto> getMedicalProviders() {
    	List<MedicalProvider> medicalProviders = medicalProviderService.findAll();
    	return medicalProviderMapper.toDto(medicalProviders);
    }

}
