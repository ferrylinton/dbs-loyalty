package com.dbs.loyalty.web.controller.rest;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.domain.PriviledgeProduct;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PriviledgeProductService;
import com.dbs.loyalty.service.dto.PriviledgeProductDto;
import com.dbs.loyalty.service.mapper.PriviledgeProductMapper;
import com.dbs.loyalty.util.ErrorUtil;
import com.dbs.loyalty.util.ResponseUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for PriviledgeProduct
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.PRODUCT })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/priviledge-products")
public class PriviledgeProductRestController {

	public static final String GET_ALL_PRIVILEDGE_PRODUCTS = "GetAllPriviledgeProducts";
	
	public static final String GET_PRIVILEDGE_PRODUCT_BY_ID = "GetPriviledgeProductById";
	
	public static final String GET_PRIVILEDGE_PRODUCT_IMAGE_BY_ID = "GetPriviledgeProductImageById";
	
	public static final String GET_PRIVILEDGE_PRODUCT_TERM_BY_ID = "GetPriviledgeProductTermById";
	
	private final ImageService imageService;
	
	private final PriviledgeProductService productService;
	
	private final PriviledgeProductMapper productMapper;

	@ApiOperation(
			nickname=GET_ALL_PRIVILEDGE_PRODUCTS, 
			value=GET_ALL_PRIVILEDGE_PRODUCTS, 
			notes="Get All Product Priviledges",
    		produces="application/json", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PriviledgeProductDto.class, responseContainer = "List")})
	@EnableLogAuditCustomer(operation=GET_ALL_PRIVILEDGE_PRODUCTS)
	@GetMapping
    public List<PriviledgeProductDto> getAll(HttpServletRequest request, HttpServletResponse response){
		return productMapper.toDto(productService.findAll());
    }
	
	@ApiOperation(
			nickname=GET_PRIVILEDGE_PRODUCT_BY_ID, 
			value=GET_PRIVILEDGE_PRODUCT_BY_ID, 
			notes="Get Product Priviledge by Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = PriviledgeProductDto.class)})
	@EnableLogAuditCustomer(operation=GET_PRIVILEDGE_PRODUCT_BY_ID)
    @GetMapping("/{id}")
    public PriviledgeProductDto getById(
    		@ApiParam(name = "id", value = "Product Id", example = "5uox4w6t2fMaldCtRWmh2E")
    		@PathVariable String id,
    		HttpServletRequest request, 
    		HttpServletResponse response) throws NotFoundException{
    	
		Optional<PriviledgeProduct> current = productService.findById(id);
    	
    	if(current.isPresent()) {
    		return productMapper.toDto(current.get());
    	}else {
    		throw ErrorUtil.createNPE(ErrorUtil.PRODUCT, id);
    	}
    }
    
	@ApiOperation(
			nickname=GET_PRIVILEDGE_PRODUCT_IMAGE_BY_ID, 
			value=GET_PRIVILEDGE_PRODUCT_IMAGE_BY_ID, 
			notes="Get Product Priviledge Image by Id",
			produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
	@EnableLogAuditCustomer(operation=GET_PRIVILEDGE_PRODUCT_IMAGE_BY_ID)
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImageById(
    		@ApiParam(name = "id", value = "PriviledgeProduct Id", example = "5uox4w6t2fMaldCtRWmh2E")
    		@PathVariable String id,
    		HttpServletRequest request, 
    		HttpServletResponse response) throws NotFoundException{
    	
    	Optional<FileImage> fileImage = imageService.findById(id);
    	
    	if(fileImage.isPresent()) {
    		return ResponseUtil.createImageResponse(fileImage.get());
    	}else {
    		throw ErrorUtil.createNPE(ErrorUtil.PRODUCT, id);
    	}
    }
	
	@ApiOperation(
			nickname=GET_PRIVILEDGE_PRODUCT_TERM_BY_ID, 
			value=GET_PRIVILEDGE_PRODUCT_TERM_BY_ID, 
			notes="Get Product Priviledge Term And Condition by Id",
    		produces=MediaType.TEXT_PLAIN_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = String.class)})
	@EnableLogAuditCustomer(operation=GET_PRIVILEDGE_PRODUCT_TERM_BY_ID)
	@GetMapping("/{id}/term")
    public String getTermAndConditionById(
    		@ApiParam(name = "id", value = "Product Id", example = "5uox4w6t2fMaldCtRWmh2E")
    		@PathVariable String id,
    		HttpServletRequest request, 
    		HttpServletResponse response) throws NotFoundException{
    	
		Optional<String> current = productService.findTermAndConditionById(id);
    	
    	if(current.isPresent()) {
    		return current.get();
    	}else {
    		throw ErrorUtil.createNPE(ErrorUtil.PRODUCT, id);
    	}
    }
	
}
