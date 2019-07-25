package com.dbs.loyalty.web.controller.rest;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.MedicalProviderCity;
import com.dbs.loyalty.service.MedicalProviderCityService;
import com.dbs.loyalty.service.dto.MedicalProviderCityDto;
import com.dbs.loyalty.service.dto.MedicalProviderDto;
import com.dbs.loyalty.service.mapper.MedicalProviderCityMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Medical Provider City.
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.WELLNESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/medical-provider-cities")
public class MedicalProviderCityRestController {
	
	public static final String GET_MEDICAL_PROVIDER_CITIES = "GetMedicalProviderCities";

    private final MedicalProviderCityService medicalProviderCityService;
    
    private final MedicalProviderCityMapper medicalProviderCityMapper;
    
    /**
     * GET  /api/medical-provider-cities : get all medical provider cities
     *
     * @return the list of medical provider cities
     */
    @ApiOperation(
    		nickname=GET_MEDICAL_PROVIDER_CITIES, 
    		value=GET_MEDICAL_PROVIDER_CITIES, 
    		produces=SwaggerConstant.JSON, 
    		authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=SwaggerConstant.OK, response=MedicalProviderDto.class)})
    @LogAuditApi(name=GET_MEDICAL_PROVIDER_CITIES)
    @GetMapping("/{medicalProviderId}")
    public List<MedicalProviderCityDto> getMedicalProviderCities(
    		@ApiParam(name = "MedicalProviderId", value = "MedicalProviderId", example = "6nJfmxAD6GWtsehXfSkShg")
    		@PathVariable String medicalProviderId) {
    	
    	List<MedicalProviderCity> medicalProviderCities = medicalProviderCityService.findByMedicalProviderId(medicalProviderId);
    	return medicalProviderCityMapper.toDto(medicalProviderCities);
    }

}
