package com.dbs.loyalty.web.controller.rest;


import java.io.IOException;
import java.util.Optional;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.aop.LogAuditApi;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.util.SecurityUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { SwaggerConstant.CUSTOMER })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/customers")
public class CustomerImageRestController {
	
	public static final String GET_CUSTOMER_IMAGE = "GetCustomerImage";

	public static final String UPDATE_CUSTOMER_IMAGE = "UpdateCustomerImage";
	
	private final ImageService imageService;
    
	@ApiOperation(
			nickname=GET_CUSTOMER_IMAGE, 
			value=GET_CUSTOMER_IMAGE, 
			produces=SwaggerConstant.IMAGE, 
			authorizations={@Authorization(value=SwaggerConstant.JWT)})
	@ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response = Byte.class)})
	@LogAuditApi(name=GET_CUSTOMER_IMAGE)
	@GetMapping("/image")
	public ResponseEntity<byte[]> getCustomerImage() throws NotFoundException {
    	Optional<FileImage> fileImage = imageService.findById(SecurityUtil.getId());
    	
		if(fileImage.isPresent()) {
			HttpHeaders headers = new HttpHeaders();
			headers.setCacheControl(CacheControl.noCache().getHeaderValue());
			headers.setContentType(MediaType.valueOf(fileImage.get().getContentType()));
			
			return ResponseEntity
					.ok()
					.headers(headers)
					.body(fileImage.get().getBytes());
		}else {
			throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, SwaggerConstant.CUSTOMER, SecurityUtil.getId()));
		}
	}
	
	@ApiOperation(
			nickname=UPDATE_CUSTOMER_IMAGE, 
			value=UPDATE_CUSTOMER_IMAGE, 
			consumes=SwaggerConstant.FORM, 
			produces=SwaggerConstant.IMAGE, 
			authorizations={@Authorization(value=SwaggerConstant.JWT)})
	@ApiResponses(value = { @ApiResponse(code=200, message=SwaggerConstant.OK, response = Byte.class)})
	@LogAuditApi(name=UPDATE_CUSTOMER_IMAGE)
	@PutMapping("/image")
    public ResponseEntity<byte[]> updateCustomerImage(
    		@ApiParam(name = "file", value = "Customer's image") 
    		@RequestParam("file") MultipartFile file) throws NotFoundException, IOException, BadRequestException  {
    	
    	if(file.isEmpty()) {
    		throw new BadRequestException(MessageConstant.FILE_IS_EMPTY);
    	}else {
    		FileImage fileImage = imageService.updateCustomerImage(file);
        	HttpHeaders headers = new HttpHeaders();
    		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
    		headers.setContentType(MediaType.valueOf(fileImage.getContentType()));
    		
    		return ResponseEntity
    				.ok()
    				.headers(headers)
    				.body(fileImage.getBytes());
    	}
    }
    
}
