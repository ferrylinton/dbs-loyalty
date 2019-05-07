package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.SwaggerConstant.CART;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;

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
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.ProductService;
import com.dbs.loyalty.service.dto.ProductDto;
import com.dbs.loyalty.service.mapper.ProductMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Product
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { CART })
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductRestController {

	private static final String NOT_FOUND_FORMAT = "Product [id=%s] is not found";
	
	private final ImageService imageService;
	
	private final ProductService productService;
	
	private final ProductMapper productMapper;

	@ApiOperation(
			nickname="GetAllByProductCategoryId", 
			value="GetAllByProductCategoryId", 
			notes="Get Products by Product Category's Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = ProductDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/products/product-categories/{productCategoryId}")
    public List<ProductDto> getAllByProductCategoryId(
    		@ApiParam(name = "productCategoryId", value = "Product Category Id", example = "7fCVdod3dfO8qFp55jaWww")
    		@PathVariable String productCategoryId){
    	
		return productService
				.findByProductCategoryId(productCategoryId)
				.stream()
				.map(product -> productMapper.toDto(product))
				.collect(Collectors.toList());
    }
	
	@ApiOperation(
			nickname="GetProductById", 
			value="GetProductById", 
			notes="Get Product by Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = ProductDto.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/products/{id}")
    public ProductDto getById(
    		@ApiParam(name = "id", value = "Product Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<ProductDto> current = productService
				.findById(id)
				.map(product -> productMapper.toDto(product));
    	
    	if(current.isPresent()) {
    		return current.get();
    	}else {
    		throw new NotFoundException(String.format(NOT_FOUND_FORMAT, id));
    	}
    }
    
	@ApiOperation(
			nickname="GetProductImageById", 
			value="GetProductImageById", 
			notes="Get Product Image by Id",
			produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/products/{id}/image")
    public ResponseEntity<byte[]> getImageById(
    		@ApiParam(name = "id", value = "Product Id", example = "zO0dDp9K")
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
			nickname="GetProductTermById", 
			value="GetProductTermById", 
			notes="Get Product Term And Condition by Id",
    		produces=MediaType.TEXT_PLAIN_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = String.class)})
	@PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/products/{id}/term")
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
