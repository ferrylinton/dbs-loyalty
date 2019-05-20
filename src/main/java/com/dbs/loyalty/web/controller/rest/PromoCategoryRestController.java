package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.GET_PROMO_CATEGORIES;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JSON;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.PROMO_CATEGORY;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(tags = { PROMO_CATEGORY })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api")
public class PromoCategoryRestController {

    private final PromoCategoryService promoCategoryService;
    
    private final PromoCategoryMapper promoCategoryMapper;

    /**
     * GET  /promo-categories : get all the Promo Categories.
     *
     * @return the list of Promo Categories
     */
    @ApiOperation(value=GET_PROMO_CATEGORIES, produces=JSON, authorizations={@Authorization(value=JWT)})
    @ApiResponses(value={@ApiResponse(code=200, message=OK, response=PromoCategoryDto.class)})
    @GetMapping("/promo-categories")
    public List<PromoCategoryDto> getPromoCategories() {
    	return promoCategoryService
    			.findAll()
    			.stream()
				.map(promoCategory -> promoCategoryMapper.toDto(promoCategory))
				.collect(Collectors.toList());
    }

}
