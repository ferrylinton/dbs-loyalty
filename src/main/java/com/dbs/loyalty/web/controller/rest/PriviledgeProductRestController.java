package com.dbs.loyalty.web.controller.rest;

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
	
	private final ImageService imageService;
	
	private final PriviledgeProductService productService;
	
	private final ProductPriviledgeMapper productMapper;

	@ApiOperation(
			nickname="GetAllProductPriviledges", 
			value="GetAllProductPriviledges", 
			notes="Get All Product Priviledges",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = ProductPriviledgeDto.class)})
    @GetMapping
    public List<ProductPriviledgeDto> getAllProductPriviledges(){
		return productService
				.findAll()
				.stream()
				.map(product -> productMapper.toDto(product))
				.collect(Collectors.toList());
    }
	
	@ApiOperation(
			nickname="GetProductPriviledgeById", 
			value="GetProductPriviledgeById", 
			notes="Get Product Priviledge by Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = ProductPriviledgeDto.class)})
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
			nickname="GetProductPriviledgeImageById", 
			value="GetProductPriviledgeImageById", 
			notes="Get Product Priviledge Image by Id",
			produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
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
			nickname="GetProductPriviledgeTermById", 
			value="GetProductPriviledgeTermById", 
			notes="Get Product Priviledge Term And Condition by Id",
    		produces=MediaType.TEXT_PLAIN_VALUE, 
    		authorizations = { @Authorization(value=JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = String.class)})
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
