package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.CART;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.domain.ProductCategory;
import com.dbs.loyalty.service.ProductCategoryService;
import com.dbs.loyalty.service.dto.ProductCategoryDto;
import com.dbs.loyalty.service.mapper.ProductCategoryMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for managing ProductCategory.
 */
@Api(tags = { CART })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductCategoryRestController {

    private final ProductCategoryService productCategoryService;
    
    private final ProductCategoryMapper productCategoryMapper;

    /**
     * GET  /product-categories : get all the productCategories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productCategories in body
     */
    @ApiOperation(
    		nickname="GetProductCategories", 
    		value="GetProductCategories", 
    		notes="Get all Product Category",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = ProductCategory.class)})
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/product-categories")
    public List<ProductCategoryDto> getProductCategories() {
    	return productCategoryService
    			.findAll()
    			.stream()
				.map(productCategory -> productCategoryMapper.toDto(productCategory))
				.collect(Collectors.toList());
    }

}
