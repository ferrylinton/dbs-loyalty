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
 * REST controller for Promo Categories API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.PROMO_CATEGORY })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/promo-categories")
public class PromoCategoryRestController {

	public static final String GET_PROMO_CATEGORIES = "GetPromoCategories";
	
    private final PromoCategoryService promoCategoryService;
    
    private final PromoCategoryMapper promoCategoryMapper;

    /**
     * GET  /promo-categories : get all the Promo Categories.
     *
     * @return the list of Promo Categories
     */
    @ApiOperation(
    		value=GET_PROMO_CATEGORIES, 
    		produces="application/json", 
    		authorizations={@Authorization(value="JWT")})
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response=PromoCategoryDto.class)})
    @EnableLogAuditCustomer(operation=GET_PROMO_CATEGORIES)
    @GetMapping
    public List<PromoCategoryDto> getPromoCategories(HttpServletRequest request, HttpServletResponse response) {
    	return promoCategoryMapper.toDto(promoCategoryService.findAll());
    }

}
