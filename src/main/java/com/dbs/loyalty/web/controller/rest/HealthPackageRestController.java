package com.dbs.loyalty.web.controller.rest;


import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.HealthPackage;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.HealthPackageService;
import com.dbs.loyalty.service.dto.HealthPackageDto;
import com.dbs.loyalty.service.mapper.HealthPackageMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Health Package.
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.WELLNESS })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/health-packages")
public class HealthPackageRestController {

	public static final String GET_HEALTH_PACKAGES = "GetHealthPackages";
	
	public static final String GET_HEALTH_PACKAGE_CONTENT_BY_ID = "GetHealthPackageContentById";

    private final HealthPackageService healthPackageService;
    
    private final HealthPackageMapper healthPackageMapper;
    
    /**
     * GET  /api/health-packages : get all health packages
     *
     * @return the list of health packages
     */
    @ApiOperation(
    		nickname=GET_HEALTH_PACKAGES, 
    		value=GET_HEALTH_PACKAGES, 
    		produces=SwaggerConstant.JSON, 
    		authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=SwaggerConstant.OK, response=HealthPackageDto.class)})
    @LogAuditApi(name=GET_HEALTH_PACKAGES)
    @GetMapping
    public List<HealthPackageDto> getHealthPackages() {
    	List<HealthPackage> healthPackages = healthPackageService.findAll();
    	return healthPackageMapper.toDto(healthPackages);
    }

    @ApiOperation(
    		nickname=GET_HEALTH_PACKAGE_CONTENT_BY_ID, 
    		value=GET_HEALTH_PACKAGE_CONTENT_BY_ID, 
    		produces=SwaggerConstant.TEXT, 
    		authorizations={@Authorization(value=SwaggerConstant.JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=SwaggerConstant.OK, response=String.class)})
    @LogAuditApi(name=GET_HEALTH_PACKAGE_CONTENT_BY_ID)
    @GetMapping(value="/{id}/content", produces=SwaggerConstant.TEXT)
    public String getContentById(
    		@ApiParam(name = "id", value = "Health Package Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<HealthPackage> current = healthPackageService.findById(id);
    	
    	if(current.isPresent()) {
    		return current.get().getContent();
    	}else {
    		throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, DomainConstant.HEALTH_PACKAGE, id));
    	}
    }
    
}
