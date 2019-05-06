package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_WITH_VALUE_NOT_FOUND;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.WELLNESS;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.HealthPartnerService;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.dto.HealthPartnerDto;
import com.dbs.loyalty.service.mapper.HealthPartnerMapper;
import com.dbs.loyalty.util.MessageUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Health Partner.
 */
@Api(tags = { WELLNESS })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HealthPartnerRestController {

    private final HealthPartnerService healthPartnerService;
    
    private final HealthPartnerMapper healthPartnerMapper;
    
    private final ImageService imageService;

    /**
     * GET  /api/healthpartners : get all health partners
     *
     * @return the ResponseEntity with status 200 (OK) and the list of health partners
     */
    @ApiOperation(
    		nickname="GetHealthPartners", 
    		value="GetHealthPartners", 
    		notes="Get all Health Partners",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = HealthPartnerDto.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/health-partners")
    public ResponseEntity<List<HealthPartnerDto>> getHealthPartners() {
    	List<HealthPartnerDto> healthPartners = healthPartnerService
    			.findAll()
    			.stream()
				.map(healthPartner -> healthPartnerMapper.toDto(healthPartner))
				.collect(Collectors.toList());
    	
    	return ResponseEntity.ok().body(healthPartners);
    }

    @ApiOperation(
    		nickname="GetHealthPartnerImage", 
    		value="GetHealthPartnerImage", 
    		notes="Get Health Partner Image", 
    		produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/health-partners/{id}/image")
	public ResponseEntity<byte[]> getHealthPartnerImage(
    		@ApiParam(name = "id", value = "HealthPartner Id", example = "77UTTDWJX3zNWABg9ixZX9")
    		@PathVariable String id) throws NotFoundException, IOException{
		
    	Optional<FileImage> fileImage = imageService.findById(id);
    	
		if(fileImage.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.valueOf(fileImage.get().getContentType()));
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(fileImage.get().getBytes());
		}else {
			String message = MessageUtil.getMessage(DATA_WITH_VALUE_NOT_FOUND, id);
			throw new NotFoundException(message);
		}
	}
}
