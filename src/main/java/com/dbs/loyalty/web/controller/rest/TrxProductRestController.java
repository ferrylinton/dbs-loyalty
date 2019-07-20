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

import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.SettingService;
import com.dbs.loyalty.service.TrxProductService;
import com.dbs.loyalty.service.dto.TrxProductDto;
import com.dbs.loyalty.service.mapper.TrxProductMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for TrxProduct
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.TRX_PRODUCT })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/trx-products")
public class TrxProductRestController {

	private static final String NOT_FOUND_FORMAT = "TrxProduct [id=%s] is not found";
	
	private final ImageService imageService;
	
	private final TrxProductService trxProductService;
	
	private final SettingService settingService;
	
	private final TrxProductMapper trxProductMapper;

	@ApiOperation(
			nickname="GetAllTrxProducts", 
			value="GetAllTrxProducts", 
			notes="Get All Bank Transaction Product",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = TrxProductDto.class)})
    @GetMapping
    public List<TrxProductDto> getAllTrxProducts(){
		return trxProductService
				.findAll()
				.stream()
				.map(product -> trxProductMapper.toDto(product, settingService))
				.collect(Collectors.toList());
    }
	
	@ApiOperation(
			nickname="GetTrxProductById", 
			value="GetTrxProductById", 
			notes="Get Product Priviledge by Id",
    		produces=MediaType.APPLICATION_JSON_VALUE, 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = TrxProductDto.class)})
    @GetMapping("/{id}")
    public TrxProductDto getById(
    		@ApiParam(name = "id", value = "Product Id", example = "zO0dDp9K")
    		@PathVariable String id) throws NotFoundException{
    	
		Optional<TrxProductDto> current = trxProductService
				.findById(id)
				.map(product -> trxProductMapper.toDto(product, settingService));
    	
    	if(current.isPresent()) {
    		return current.get();
    	}else {
    		throw new NotFoundException(String.format(NOT_FOUND_FORMAT, id));
    	}
    }
    
	@ApiOperation(
			nickname="GetTrxProductImageById", 
			value="GetTrxProductImageById", 
			notes="Get Bank Transaction Product Image by Id",
			produces= "image/png, image/jpeg", 
    		authorizations = { @Authorization(value=SwaggerConstant.JWT) })
    @ApiResponses(value={@ApiResponse(code=200, message="OK", response = Byte.class)})
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getTrxProductImageById(
    		@ApiParam(name = "id", value = "TrxProduct Id", example = "zO0dDp9K")
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
	
}
