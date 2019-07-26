package com.dbs.loyalty.web.controller.rest;

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

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.PriviledgeProductService;
import com.dbs.loyalty.service.dto.ProductPriviledgeDto;
import com.dbs.loyalty.service.mapper.ProductPriviledgeMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for ProductPriviledge
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

	private static final String NOT_FOUND_FORMAT = "ProductPriviledge [id=%s] is not found";
	
	public static final String GET_ALL_PRODUCT_PRIVILEDGES = "GetAllProductPriviledges";
	
	public static final String GET_PRODUCT_PRIVILEDGE_BY_ID = "GetProductPriviledgeById";
	
	public static final String GET_PRODUCT_PRIVILEDGE_IMAGE_BY_ID = "GetProductPriviledgeImageById";
	
	public static final String GET_PRODUCT_PRIVILEDGE_TERM_BY_ID = "GetProductPriviledgeTermById";
	
	private final ImageService imageService;
	
	private final PriviledgeProductService productService;
	
	private final ProductPriviledgeMapper productMapper;

	@ApiOperation(
			nickname=GET_ALL_PRODUCT_PRIVILEDGES, 
			value=GET_ALL_PRODUCT_PRIVILEDGES, 
			notes="Get All Product Priviledges",
    		produces="application/json", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = ProductPriviledgeDto.class)})
	@LogAuditApi(name=GET_ALL_PRODUCT_PRIVILEDGES)
	@GetMapping
    public List<ProductPriviledgeDto> getAllProductPriviledges(){
		return productService
				.findAll()
				.stream()
				.map(product -> productMapper.toDto(product))
				.collect(Collectors.toList());
    }
	
	@ApiOperation(
			nickname=GET_PRODUCT_PRIVILEDGE_BY_ID, 
			value=GET_PRODUCT_PRIVILEDGE_BY_ID, 
			notes="Get Product Priviledge by Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = ProductPriviledgeDto.class)})
	@LogAuditApi(name=GET_PRODUCT_PRIVILEDGE_BY_ID)
    @GetMapping("/{id}")
    public ProductPriviledgeDto getById(
    		@ApiParam(name = "id", value = "Product Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<ProductPriviledgeDto> current = productService
				.findById(id)
				.map(product -> productMapper.toDto(product));
    	
    	if(current.isPresent()) {
    		return current.get();
    	}else {
    		throw new NotFoundException(String.format(NOT_FOUND_FORMAT, id));
    	}
    }
    
	@ApiOperation(
			nickname=GET_PRODUCT_PRIVILEDGE_IMAGE_BY_ID, 
			value=GET_PRODUCT_PRIVILEDGE_IMAGE_BY_ID, 
			notes="Get Product Priviledge Image by Id",
			produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
	@LogAuditApi(name=GET_PRODUCT_PRIVILEDGE_IMAGE_BY_ID)
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getProductPriviledgeImageById(
    		@ApiParam(name = "id", value = "ProductPriviledge Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
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
    		throw new NotFoundException(String.format(NOT_FOUND_FORMAT, id));
    	}
    }
	
	@ApiOperation(
			nickname=GET_PRODUCT_PRIVILEDGE_TERM_BY_ID, 
			value=GET_PRODUCT_PRIVILEDGE_TERM_BY_ID, 
			notes="Get Product Priviledge Term And Condition by Id",
    		produces=MediaType.TEXT_PLAIN_VALUE, 
    		authorizations = { @Authorization(value="JWT") })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = String.class)})
	@LogAuditApi(name=GET_PRODUCT_PRIVILEDGE_TERM_BY_ID)
	@GetMapping("/{id}/term")
    public ResponseEntity<String> getTermAndConditionById(
    		@ApiParam(name = "id", value = "Product Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<String> current = productService.findTermAndConditionById(id);
    	
    	if(current.isPresent()) {
    		HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.TEXT_PLAIN);
			
    		return ResponseEntity
    				.ok()
    				.headers(headers)
    				.body(current.get());
    	}else {
    		throw new NotFoundException(String.format(NOT_FOUND_FORMAT, id));
    	}
    }
	
}
