package com.dbs.loyalty.web.controller.rest;

import static com.dbs.loyalty.config.constant.RestConstant.GET_IMAGE_BY_PROMO_ID;
import static com.dbs.loyalty.config.constant.MessageConstant.DATA_IS_NOT_FOUND;
import static com.dbs.loyalty.config.constant.SwaggerConstant.IMAGE;
import static com.dbs.loyalty.config.constant.SwaggerConstant.JWT;
import static com.dbs.loyalty.config.constant.SwaggerConstant.OK;
import static com.dbs.loyalty.config.constant.SwaggerConstant.PROMO;

import java.util.Optional;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dbs.loyalty.config.constant.EntityConstant;
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
@Api(tags = { PROMO })
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
@RestController
@RequestMapping("/api")
public class PromoImageRestController {

	private final ImageService imageService;
	
	@ApiOperation(value=GET_IMAGE_BY_PROMO_ID, produces=IMAGE, authorizations={@Authorization(value=JWT)})
	@ApiResponses(value={@ApiResponse(code=200, message=OK, response=Byte.class)})
    @GetMapping("/promos/{id}/image")
    public ResponseEntity<byte[]> getImageByPromoId(
    		@ApiParam(name = "id", value = "Promo Id", example = "5WTqpHYs3wZoIdhAkbWt1W")
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
    		throw new NotFoundException(String.format(DATA_IS_NOT_FOUND, EntityConstant.PROMO, id));
    	}
    }
	
}
