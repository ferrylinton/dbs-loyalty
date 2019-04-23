package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.PROMO_CATEGORY;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.PromoCategory;
import com.dbs.loyalty.service.PromoCategoryService;
import com.dbs.loyalty.service.dto.PromoCategoryDto;
import com.dbs.loyalty.service.mapper.PromoCategoryMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing PromoCategory.
 */
@Api(tags = { PROMO_CATEGORY })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PromoCategoryRestController {

    private final PromoCategoryService promoCategoryService;
    
    private final PromoCategoryMapper promoCategoryMapper;

    /**
     * GET  /promo-categories : get all the promoCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of promoCategories in body
     */
    @ApiOperation(
    		nickname="GetPromoCategories", 
    		value="GetPromoCategories", 
    		notes="Get all Promo Category",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PromoCategory.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/promo-categories")
    public ResponseEntity<List<PromoCategoryDto>> getPromoCategories() {
    	List<PromoCategoryDto> promoCategories = promoCategoryService
    			.findAll()
    			.stream()
				.map(promoCategory -> promoCategoryMapper.toDto(promoCategory))
				.collect(Collectors.toList());
    	
        return ResponseEntity.ok().body(promoCategories);
    }

}
