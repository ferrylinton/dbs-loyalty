package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.MessageConstant.DATA_IS_NOT_FOUND;
import static com.dbs.loyalty.config.constant.MessageConstant.FILE_IS_EMPTY;
import static com.dbs.loyalty.config.constant.RestConstant.GET_CUSTOMER_IMAGE;
import static com.dbs.loyalty.config.constant.RestConstant.UPDATE_CUSTOMER_IMAGE;
import static com.dbs.loyalty.config.constant.SwaggerConstant.CUSTOMER;
import static com.dbs.loyalty.config.constant.SwaggerConstant.FORM;
import static com.dbs.loyalty.config.constant.SwaggerConstant.IMAGE;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;

import java.io.IOException;
import java.util.Optional;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.util.MessageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.web.controller.AbstractController;
import com.dbs.loyalty.web.response.BadRequestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

@Api(tags = { CUSTOMER })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
public class CustomerImageRestController extends AbstractController{
	
	private final ImageService imageService;
    
	@ApiOperation(nickname=GET_CUSTOMER_IMAGE, value=GET_CUSTOMER_IMAGE, produces=IMAGE, authorizations={@Authorization(value=JWT)})
	@ApiResponses(value = { @ApiResponse(code=200, message=OK, response = Byte.class)})
	@GetMapping("/api/customers/image")
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
			throw new NotFoundException(String.format(DATA_IS_NOT_FOUND, CUSTOMER, SecurityUtil.getId()));
		}
	}
	
	@ApiOperation(nickname=UPDATE_CUSTOMER_IMAGE, value=UPDATE_CUSTOMER_IMAGE, consumes=FORM, produces=IMAGE, authorizations={@Authorization(value=JWT)})
	@ApiResponses(value = { @ApiResponse(code=200, message=OK, response = Byte.class)})
    @PutMapping("/api/customers/image")
    public ResponseEntity<byte[]> updateCustomerImage(
    		@ApiParam(name = "file", value = "Customer's image") 
    		@RequestParam("file") MultipartFile file) throws NotFoundException, IOException, BadRequestException  {
    	
    	if(file.isEmpty()) {
    		throw new BadRequestException(new BadRequestResponse(MessageUtil.getMessage(FILE_IS_EMPTY)));
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
