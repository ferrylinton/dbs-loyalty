package com.dbs.loyalty.web.controller.rest;


import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.BadRequestException;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;
import com.dbs.loyalty.service.LogAuditCustomerService;
import com.dbs.loyalty.util.ImageUtil;
import com.dbs.loyalty.util.SecurityUtil;
import com.dbs.loyalty.util.UrlUtil;

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
	
	 private final LogAuditCustomerService logAuditCustomerService;
    
	@ApiOperation(
			nickname=GET_CUSTOMER_IMAGE, 
			value=GET_CUSTOMER_IMAGE, 
			produces="image/png, image/jpeg", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value = { @ApiResponse(code=200, message="OK", response = Byte.class)})
	@EnableLogAuditCustomer(operation=GET_CUSTOMER_IMAGE)
	@GetMapping("/image")
	public ResponseEntity<byte[]> getCustomerImage(HttpServletRequest request, HttpServletResponse response) throws NotFoundException {
    	Optional<FileImage> fileImage = imageService.findById(SecurityUtil.getId());
    	
		if(fileImage.isPresent()) {
			return ImageUtil.getResponse(fileImage.get());
		}else {
			throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, SwaggerConstant.CUSTOMER, SecurityUtil.getId()));
		}
	}
	
	@ApiOperation(
			nickname=UPDATE_CUSTOMER_IMAGE, 
			value=UPDATE_CUSTOMER_IMAGE, 
			consumes="multipart/form-data", 
			produces="image/png, image/jpeg", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value = { @ApiResponse(code=200, message="OK", response = Byte.class)})
	@PutMapping("/image")
    public ResponseEntity<byte[]> updateCustomerImage(
    		@ApiParam(name = "file", value = "Customer's image") 
    		@RequestParam("file") MultipartFile file,
    		HttpServletRequest request) throws NotFoundException, IOException, BadRequestException  {
    	
    	if(file.isEmpty()) {
    		throw new BadRequestException(MessageConstant.FILE_IS_EMPTY);
    	}else {
    		FileImage fileImage = new FileImage();
    		Optional<FileImage> current = imageService.findById(SecurityUtil.getId());
    		byte[] oldData = null;
    		
    		if(current.isPresent()) {
    			oldData = current.get().getBytes().clone();
    			fileImage = current.get();
    			fileImage.setLastModifiedBy(SecurityUtil.getLogged());
    			fileImage.setLastModifiedDate(Instant.now());
    		}else {
    			fileImage.setCreatedBy(SecurityUtil.getLogged());
    			fileImage.setCreatedDate(Instant.now());
    		}
    		
    		ImageUtil.setFileImage(fileImage, file);
    		fileImage = imageService.save(fileImage);
    		logAuditCustomerService.saveFile(UPDATE_CUSTOMER_IMAGE, UrlUtil.getFullUrl(request), file.getBytes(), oldData);
    		return ImageUtil.getResponse(fileImage);
    	}
    }

}
