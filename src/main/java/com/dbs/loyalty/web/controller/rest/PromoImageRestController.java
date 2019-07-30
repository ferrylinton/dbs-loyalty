package com.dbs.loyalty.web.controller.rest;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.aop.EnableLogAuditCustomer;
import com.dbs.loyalty.config.constant.DomainConstant;
import com.dbs.loyalty.config.constant.MessageConstant;
import com.dbs.loyalty.config.constant.SwaggerConstant;
import com.dbs.loyalty.domain.FileImage;
import com.dbs.loyalty.exception.NotFoundException;
import com.dbs.loyalty.service.ImageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for Promo's Image API
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 * 
 */
@Api(tags = { SwaggerConstant.PROMO })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api/promos")
public class PromoImageRestController {

	public static final String GET_PROMO_IMAGE_BY_ID = "GetPromoImageById";
	
	private final ImageService imageService;
	
	@ApiOperation(
			value=GET_PROMO_IMAGE_BY_ID, 
			produces="image/png, image/jpeg", 
			authorizations={@Authorization(value="JWT")})
	@ApiResponses(value={@ApiResponse(code=200, message="OK", response=Byte.class)})
	@EnableLogAuditCustomer(operation=GET_PROMO_IMAGE_BY_ID)
	@GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImageByPromoId(
    		@ApiParam(name = "id", value = "Promo Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
    		@PathVariable String id,
    		HttpServletRequest request, HttpServletResponse response) throws NotFoundException{
    	
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
    		throw new NotFoundException(String.format(MessageConstant.DATA_IS_NOT_FOUND, DomainConstant.PROMO, id));
    	}
    }
	
}
